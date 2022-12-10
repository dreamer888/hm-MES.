package com.dream.iot.server.protocol;

import com.dream.iot.*;
import com.dream.iot.business.BusinessFactory;
import com.dream.iot.client.ClientProxySync;
import com.dream.iot.client.ClientProxyServerProtocol;
import com.dream.iot.proxy.ProxyClientMessageUtil;
import com.dream.iot.proxy.ProxyServerMessage;
import com.dream.iot.proxy.ProxyClientResponseBody;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.server.SocketServerComponent;
import com.dream.iot.server.ServerSocketProtocol;
import com.dream.iot.server.ServerMessage;
import com.dream.iot.server.manager.DevicePipelineManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 用来声明此协议是由服务端主动向客户端发起的
 * @param <M>
 */
public abstract class ServerInitiativeProtocol<M extends ServerMessage> extends
        ServerSocketProtocol<M> implements ClientProxySync<ServerInitiativeProtocol> {

    private boolean clientStart;

    // 默认超时时间10秒
    private long timeout = 10 * 1000;

    private CountDownLatch downLatch; //同步锁
    private ClientProxyServerProtocol serverProtocol;
    private FreeProtocolHandle freeProtocolHandle;

    /**执行失败原因*/
    private String reason;
    /** 协议回调状态 */
    private ExecStatus execStatus = ExecStatus.success;

    @Override
    public AbstractProtocol exec(BusinessFactory factory) {

        /**
         * @see #request()
         * @see #isSyncRequest() 如果是同步请求交由调用线程执行 如果是异步调用则交由netty工作线程处理
         */
        if(!isSyncRequest()) {
            return this.exec(getProtocolHandle());
        } else {
            // 对于同步请求没有在主线程释放的问题 https://gitee.com/iteaj/iot/issues/I517T3
            this.releaseLock(); // 释放锁
        }

        return null;
    }

    /**
     * 构建请求报文
     * @return
     */
    public ServerInitiativeProtocol buildRequestMessage() {
        try {
            this.requestMessage = this.doBuildRequestMessage();
        } catch (IOException e) {
            throw new ProtocolException("构建请求报文异常", e.getCause());
        }
        return this;
    }

    /**
     * 构建服务端要请求客户端的报文
     * @return
     */
    protected abstract M doBuildRequestMessage() throws IOException;

    /**
     * 对于平台主动向外请求的协议 由于构建响应报文必须等到响应报文的到来<br>
     *     所以此方法不能使用,请使用以下方法
     * @see #setResponseMessage(SocketMessage) 为响应的报文
     * @return
     */
    @Override
    public ServerInitiativeProtocol buildResponseMessage() {
        if(logger.isDebugEnabled() && this.responseMessage() != null) {
            Message.MessageHead head = this.responseMessage().getHead();
            String messageId = head != null ? head.getMessageId() : null;
            logger.debug("服务端主动协议 客户端响应平台 - 客户端编号: {} - 协议类型: {} - messageId: {} - 报文: {}"
                    , getEquipCode(), protocolType().getType(), messageId, this.responseMessage());
        }

        doBuildResponseMessage(responseMessage()); return this;
    }

    /**
     * 解析客户端响应给服务端的报文
     * @param message
     */
    protected abstract void doBuildResponseMessage(M message);

    /**
     * 用来做为将请求报文和响应报文进行关联的key
     * 默认用报文头的{@link com.dream.iot.Message.MessageHead#getMessageId()}作为key
     * @return
     */
    public Object relationKey(){
        return requestMessage().getHead().getMessageId();
    }

    /**
     * 平台主动向外发起请求
     */
    public <P extends ServerInitiativeProtocol<M>> void request(FreeProtocolHandle<P> handle) throws ProtocolException {
        this.freeProtocolHandle = handle;
        request();
    }

    /**
     * 平台主动向外发起请求
     */
    public void request() throws ProtocolException {
        try {
            this.buildRequestMessage();

            //构建完请求协议必须验证请求数据是否存在
            if(null == requestMessage()) {
                throw new IllegalStateException("不存在请求报文");
            }

            //获取客户端管理器
            SocketServerComponent component = IotServeBootstrap.getServerComponent(requestMessage().getClass());
            if(component == null) {
                throw new ProtocolException("获取不到["+responseMessage().getClass().getSimpleName()+"]对应的组件");
            }

            ChannelFuture future = writeAndFlush(component);
            if (future == null) {
                // 写失败, 直接执行业务
                exec(getProtocolHandle());
                return;
            }

            /**
             * 同步请求阻塞线程等待
             * @see #getTimeout() 等待超时
             */
            if(isSyncRequest()) {
                syncDeadValidate(future.channel());

                boolean await = getDownLatch().await(getTimeout(), TimeUnit.MILLISECONDS);
                if(!await) {
                    this.execTimeoutHandle(component);
                }

                // 执行业务
                exec(getProtocolHandle());
            }

        } catch (InterruptedException e) {
            throw new ProtocolException("执行中断", e);
        } catch (Exception e) {
            if(e instanceof ProtocolException) {
                throw e;
            } else {
                throw new ProtocolException("请求异常", e);
            }
        }
    }

    /**
     * 写报文到客户端
     * @param component
     * @return
     * @throws InterruptedException
     */
    protected ChannelFuture writeAndFlush(SocketServerComponent component) throws InterruptedException {
        Optional<ChannelFuture> optional = component.writeAndFlush(getEquipCode(), this);
        ChannelFuture future = optional.orElse(null);

        //先等待写出完成
        if(optional.isPresent()) {
            boolean await = future.await(getTimeout(), TimeUnit.MILLISECONDS);
            if(!await) {
                setExecStatus(ExecStatus.timeout);
            }

            if(future.cause() != null) {
                this.setExecStatus(ExecStatus.fail);
                this.reason = future.cause().getMessage();
            }
        } else {
            this.reason = "设备断线或不在线";
            setExecStatus(ExecStatus.offline);
        }

        if(getExecStatus() != ExecStatus.success) {
            return null;
        } else if(logger.isDebugEnabled()){
            M message = requestMessage();
            logger.debug("服务端主动协议 平台请求客户端 - 客户端编号: {} - 协议类型: {} - messageId: {} - 报文: {}",
                    getEquipCode(), protocolType(), message.getHead().getMessageId(), message);
        }

        return future;
    }

    /**
     * 同步时的死锁校验
     * 对于同步请求, 当前同步线程和{@code Channel}的工作线程不能是同一个
     * @param channel
     */
    protected void syncDeadValidate(Channel channel) {
        if(channel.eventLoop().inEventLoop()) {
            throw new IllegalThreadStateException("同步线程和工作线程相同将导致死锁");
        }
    }

    protected void execTimeoutHandle(SocketServerComponent component) {
        setExecStatus(ExecStatus.timeout);
        if(isRelation()) { // 移除掉对应的协议
            try {
                syncRemoveTimeoutProtocol(component);
            } finally {
                // 释放锁
                releaseLock();
            }
        }
    }

    /**
     * 这里的移除将比超时管理器早
     * @see com.dream.iot.server.ServerTimeoutProtocolManager#protocolTimeoutValidate(ProtocolTimeoutStorage) 协议超时管理
     */
    protected void syncRemoveTimeoutProtocol(SocketServerComponent component) {
        Message.MessageHead head = requestMessage().getHead();

        Object protocol = component.protocolFactory().get(head.getMessageId());

        if(protocol != null && protocol == this) {
            component.protocolFactory().remove(head.getMessageId());
            if(logger.isWarnEnabled()) {
                logger.warn("同步超时({}) 超时移除({}ms) - 客户端编号: {} - messageId: {} - 协议类型: {}"
                        , component.getName(), getTimeout(), head.getEquipCode(), head.getMessageId(), protocolType());
            }
        }
    }

    /**
     * 同步调用, 调用的线程将阻塞, 直到超时或者客户端响应
     * @see #releaseLock()
     * @param timeout 阻塞超时时间
     * @return
     */
    public ServerInitiativeProtocol sync(long timeout) {
        setDownLatch(new CountDownLatch(1));
        return timeout(timeout);
    }

    @Override
    public ServerInitiativeProtocol exec(ProtocolHandle handle) {
        // 构建响应报文
        // 不在主线程调用问题 https://gitee.com/iteaj/iot/issues/I517T3
        // 不在主线程捕获问题 https://gitee.com/iteaj/iot/issues/I517Y0
        this.buildResponseMessage();

        Object respObject = null;
        if(handle != null) {
            respObject = handle.handle(this);
        }

        // 验证是否需要响应报文给应用程序客户端
        if(isClientStart()) {
            this.proxyClientHandle(respObject);
        }

        return null;
    }

    public boolean isClientStart() {
        return clientStart;
    }

    public ServerInitiativeProtocol setClientStart(boolean clientStart) {
        this.clientStart = clientStart;
        return this;
    }

    /**
     * @see #clientStart true 说明此次请求是由应用程序客户端发起的调用
     * @see ClientProxySync#getProxyProtocol() () 返回的值不为空, 说明需要响应调用的应用客户端
     * @param clientResp
     */
    protected void proxyClientHandle(Object clientResp) {
        final ClientProxyServerProtocol clientProxyServerProtocol = this.getProxyProtocol();

        if(clientProxyServerProtocol != null) {

            ProxyClientResponseBody responseBody;
            // 用户自定义响应报文
            if(clientResp instanceof ProxyClientResponseBody) {
                responseBody = (ProxyClientResponseBody) clientResp;
                if(responseBody.getStatus() == null) {
                    responseBody.setStatus(getExecStatus());
                }

                if(responseBody.getReason() == null) {
                    responseBody.setReason("success");
                }
            } else { // 使用默认报文
                ExecStatus execStatus = getExecStatus();
                String reason = execStatus.desc;
                if(execStatus != ExecStatus.success) {
                    String equipCode = getEquipCode();
                    if(execStatus == ExecStatus.offline) {
                        reason = "["+ equipCode +"]不在线";
                    } else if(execStatus == ExecStatus.timeout) {
                        reason = "["+ equipCode +"]响应超时";
                    }
                }

                responseBody = new ProxyClientResponseBody(reason, execStatus);
            }

            final ProxyServerMessage requestMessage = clientProxyServerProtocol.requestMessage();
            final String deviceSn = requestMessage.getBody().getDeviceSn();
            final Object tradeType = requestMessage.getBody().getCtrl();
            final String messageId = requestMessage.getHead().getMessageId();

            // 构建响应给客户端的报文信息
            ProxyServerMessage responseMessage = new ProxyServerMessage(requestMessage.getHead(), responseBody);
            responseMessage = ProxyClientMessageUtil.encoder(responseMessage);
            clientProxyServerProtocol.setResponseMessage(responseMessage);

            // 写出报文到客户端
            Optional<ChannelFuture> optional = DevicePipelineManager.getInstance(ProxyServerMessage.class)
                    .writeAndFlush(clientProxyServerProtocol.getEquipCode(), clientProxyServerProtocol);
            optional.orElseThrow(() -> new ProtocolException("应答代理客户端失败, 掉线或者不存在["+clientProxyServerProtocol.getEquipCode()+"]")).addListener(future -> {
                String msg = future.isSuccess() ? "成功" : "失败";
                if(logger.isDebugEnabled()) {
                    logger.debug("平台响应代理客户端({}) 客户端编号: {} - messageId: {} - 代理客户端编号: {} - ctrl: {}", msg
                            , clientProxyServerProtocol.getEquipCode(), messageId, deviceSn, tradeType);
                }
            });
        }
    }
    protected ProtocolHandle getProtocolHandle() {
        ProtocolHandle protocolHandle = getFreeProtocolHandle();
        if(protocolHandle == null) {
            protocolHandle = IotServeBootstrap.getBusinessFactory().getProtocolHandle(getClass());
        }

        return protocolHandle;
    }

    /**
     * @see com.dream.iot.server.codec.DeviceProtocolEncoder
     * @return
     */
    @Override
    public boolean isRelation() {
        return ClientProxySync.super.isRelation();
    }

    @Override
    public ClientProxyServerProtocol getProxyProtocol() {
        return this.serverProtocol;
    }

    @Override
    public ServerInitiativeProtocol<M> setProxyProtocol(ServerSocketProtocol protocol) {
        this.serverProtocol = (ClientProxyServerProtocol) protocol;
        return this;
    }

    public long getTimeout() {
        return timeout;
    }

    public FreeProtocolHandle getFreeProtocolHandle() {
        return freeProtocolHandle;
    }

    public ServerInitiativeProtocol setFreeProtocolHandle(FreeProtocolHandle freeProtocolHandle) {
        this.freeProtocolHandle = freeProtocolHandle;
        return this;
    }

    /**
     *  设置超时时间
     * @param timeout
     */
    public ServerInitiativeProtocol timeout(long timeout) {
        if(timeout < 0) {
            throw new ProtocolException("超时时间必须大于 0(ms)");
        }

        this.timeout = timeout;
        return this;
    }

    @Override
    public abstract ProtocolType protocolType();

    public ExecStatus getExecStatus() {
        return execStatus;
    }

    public void setExecStatus(ExecStatus execStatus) {
        this.execStatus = execStatus;
    }

    /**
     * 是否进行同步请求处理,默认否
     * @return
     */
    public boolean isSyncRequest(){
        return getDownLatch() != null;
    }

    public void releaseLock() {
        if(isSyncRequest()) {
            getDownLatch().countDown();
        }
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    protected CountDownLatch getDownLatch() {
        return downLatch;
    }

    protected void setDownLatch(CountDownLatch downLatch) {
        this.downLatch = downLatch;
    }
}
