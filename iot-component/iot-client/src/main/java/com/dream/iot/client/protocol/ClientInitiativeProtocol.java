package com.dream.iot.client.protocol;

import com.dream.iot.client.*;
import com.dream.iot.*;
import com.dream.iot.business.BusinessFactory;
import com.dream.iot.consts.ExecStatus;
import io.netty.channel.ChannelFuture;
import org.springframework.util.StringUtils;

import java.nio.channels.ClosedChannelException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 首先它是一个客户端协议
 * 其次它是一个客户端主动请求服务端的协议
 * @param <C>
 */
public abstract class ClientInitiativeProtocol<C extends ClientMessage> extends ClientSocketProtocol<C> implements ProtocolPreservable {

    /**异常原因*/
    private String reason;
    private long timeout = 10 * 1000; // 超时时间, 默认10秒
    private CountDownLatch downLatch; //同步锁

    // 自定义协议处理器
    private FreeProtocolHandle freeProtocolHandle;
    /** 协议回调状态 */
    private ExecStatus execStatus = ExecStatus.success;

    /**
     * 1. 如果业务处理器存在则执行业务
     * 2. 如果是同步请求则交由业务线程处理
     * 3. 如果是异步请求则交由netty工作线程处理 这种情况最好不要执行耗时的动作
     * @param factory 协议业务
     * @return
     */
    @Override
    public AbstractProtocol exec(BusinessFactory factory) {
        ProtocolHandle protocolHandle = getProtocolHandle();

        // 如果是同步请求则交由其线程执行业务
        if(protocolHandle != null && !isSyncRequest()) {
            return this.exec(protocolHandle);
        }

        return null;
    }

    @Override
    public AbstractProtocol exec(ProtocolHandle handle) {
        if(handle != null) {
            handle.handle(this);
        } else {
            throw new ProtocolException("参数[handle]不能为null");
        }
        // 客户端主动调用的协议不需要在响应给服务端, 返回null
        return null;
    }

    @Override
    public ClientSocketProtocol buildRequestMessage() {
        this.requestMessage = doBuildRequestMessage();

        if(this.requestMessage == null) {
            throw new ProtocolException("构建请求报文失败");
        }

        if(this.requestMessage().getHead() == null || this.requestMessage().getBody() == null) {
            throw new ProtocolException("构建请求报文失败, 没有设置[MessageHead] or [MessageBody]");
        }

        return this;
    }

    protected abstract C doBuildRequestMessage();

    /**
     *
     * @return
     */
    @Override
    public ClientSocketProtocol<C> buildResponseMessage() {
        try {
            doBuildResponseMessage(responseMessage());

            return null;
        } finally {
            // 如果是同步请求必须释放锁
            this.releaseLock();
        }
    }

    public final AbstractProtocol buildResponseMessage(C responseMessage) {
        this.responseMessage = responseMessage;
        return this.buildResponseMessage();
    }

    /**
     * @param responseMessage
     */
    public abstract void doBuildResponseMessage(C responseMessage);

    /**
     * 平台主动向外发起请求
     */
    protected void sendRequest() throws ProtocolException {
        try {
            // 构建请求报文
            buildRequestMessage();

            /**
             * 获取对应的客户端 {@link #getClientKey()}
             */
            SocketClient client = getIotClient();

            /**
             * 写报文
             */
            writeAndFlush(client);

            if(getExecStatus() != ExecStatus.success) {
                this.buildResponseMessage();
            }

            /**
             * 是同步请求
             */
            if(isSyncRequest()) {
                // 检查是否会出现死锁
                syncDeadValidate(client);

                // 如果发送成功等待报文响应
                boolean await = getDownLatch().await(getTimeout(), TimeUnit.MILLISECONDS);
                if(!await) { // 响应超时
                    this.execTimeoutHandle();
                }

                // 同步执行业务
                ProtocolHandle protocolHandle = getProtocolHandle();
                if(protocolHandle != null) {
                    protocolHandle.handle(this);
                }
            } else if(!isRelation()) { // 既不是同步也不是异步, 直接执行业务
                ProtocolHandle protocolHandle = getProtocolHandle();
                if(protocolHandle != null) {
                    protocolHandle.handle(this);
                }
            }

        } catch (InterruptedException e) {
            throw new ClientProtocolException(e);
        } catch (ExecutionException e) {
            if(e.getCause() instanceof ClosedChannelException) {
                throw new ClientProtocolException("请求失败 - 连接关闭", e.getCause());
            } else {
                throw new ClientProtocolException("请求失败 - 未知错误", e);
            }
        } catch (TimeoutException e) {
            throw new ClientProtocolException("请求失败 - 超时", e);
        }
    }

    protected void writeAndFlush(SocketClient client) throws InterruptedException, ExecutionException, TimeoutException {
        // 发起请求, 写出请求报文
        ChannelFuture request = client.writeAndFlush(this);
        boolean await = request.await(getTimeout() > 0 ? getTimeout() : 3000, TimeUnit.MILLISECONDS);
        if(!await) {
            this.reason = "请求超时";
            setExecStatus(ExecStatus.timeout);
        } else if(request.cause() != null) {
            setExecStatus(ExecStatus.fail);
            this.reason = request.cause().getMessage();
        }
    }

    /**
     * 同步时的死锁校验
     * 对于同步请求, 释放锁的条件是等待tcp连接的返回<hr>
     *     如果此时锁住此线程则此tcp的读操作失效只能等待超时
     *     所以同步线程和{@code Channel}的工作线程不能是同一个
     * @param client
     */
    protected void syncDeadValidate(SocketClient client) {
        if(client.getChannel().eventLoop().inEventLoop()) {
            throw new IllegalThreadStateException("同步线程和连接工作线程相同将导致死锁");
        }
    }

    protected void execTimeoutHandle() {
        setExecStatus(ExecStatus.timeout);

        if(isRelation()) { // 移除掉对应的协议
            try {
                syncRemoveTimeoutProtocol();
            } finally {
                // 释放锁
                releaseLock();
            }
        }
    }

    /**
     * 这里的移除将比超时管理器早
     * @see ClientProtocolTimeoutManager#protocolTimeoutValidate(ProtocolTimeoutStorage) 协议超时管理
     */
    protected void syncRemoveTimeoutProtocol() {
        Message.MessageHead head = requestMessage().getHead();
        ClientComponent component = IotClientBootstrap.getClientComponentFactory()
                .getByClass(requestMessage().getClass());

        Object protocol = component.protocolFactory().remove(getMessageId());

        // 当前的协议和移除不是同一个对象(已被修改)
        if(protocol != null && protocol != this) {
            throw new ClientProtocolException("协议对象状态异常[已被修改]");
        } else if(logger.isWarnEnabled()) {
            logger.warn("协议同步超时({}) 超时移除({}ms) - 客户端编号: {} - messageId: {} - 协议类型: {}"
                    , component.getName(), getTimeout(), head.getEquipCode(), head.getMessageId(), protocolType());
        }
    }

    /**
     * 平台主动发起请求
     * @param handle 需要处理的业务
     */
    public <T extends ClientInitiativeProtocol> void request(FreeProtocolHandle<T> handle) throws ProtocolException{
        this.freeProtocolHandle = handle;
        if(this.getFreeProtocolHandle() == null) {
            throw new ClientProtocolException("[handle]不能为Null");
        }

        // 指定要执行的业务后, 必须指定超时时间
        validateTimeout(getTimeout());

        this.request();
    }

    /**
     * 请求默认的远程主机
     * @see ClientComponent#getClient()
     * @throws ProtocolException
     */
    public void request() throws ProtocolException {
        this.sendRequest();
    }

    /**
     *  请求指定的远程主机
     * @see com.dream.iot.client.component.SocketClientComponent#createNewClientAndConnect(ClientConnectProperties)
     * @param host 远程主机
     * @param port 远程端口
     */
    public void request(String host, int port) {
        if(!StringUtils.hasText(host) || port<0) {
            throw new IllegalArgumentException("未指定请求服务器的[host or port]");
        }

        this.setClientKey(new ClientConnectProperties(host, port));
        request();
    }

    /**
     *  请求指定的远程主机, 对应的连接需要先创建
     * @see com.dream.iot.client.component.SocketClientComponent#createNewClientAndConnect(ClientConnectProperties)
     * @param server 远程主机配置
     * @return 同步请求时返回当前对象 否则不返回
     */
    public void request(ClientConnectProperties server) {
        if(server == null) {
            throw new IllegalArgumentException("未指定请求服务器的参数[server]");
        }

        this.setClientKey(server);
        request();
    }

    /**
     *  请求指定的远程主机, 如果主机不存在则会先创建{@link #getIotClient()}
     * @param server 远程主机
     * @param handle 自定义处理器
     * @return
     */
    public <T extends ClientInitiativeProtocol> void request(ClientConnectProperties server, FreeProtocolHandle<T> handle) {
        if(server == null) {
            throw new IllegalArgumentException("未指定请求服务器的参数[server]");
        }
        this.setClientKey(server);
        request(handle);
    }

    protected ProtocolHandle getProtocolHandle() {
        ProtocolHandle protocolHandle = getFreeProtocolHandle();
        if(protocolHandle == null) {
            protocolHandle = getDefaultProtocolHandle();
        }

        return protocolHandle;
    }

    protected void validateTimeout(long timeout) {
        if(timeout < 0) {
            throw new ProtocolException("超时时间(timeout)必须>=0(ms)");
        }

        this.setTimeout(timeout);
    }

    protected void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * 客户端是否激活
     * @return
     */
    public boolean isActive() {
        return getIotClient().getChannel() != null
                && getIotClient().getChannel().isActive();
    }

    /**
     * 同步请求
     * @param timeout 同步超时时间
     * @return
     */
    public ClientInitiativeProtocol<C> sync(long timeout) {
        validateTimeout(timeout);
        setDownLatch(new CountDownLatch(1));
        return this;
    }

    /**
     * 设置异步超时时间
     * @see com.dream.iot.AbstractProtocolTimeoutManager 超时报文管理
     * @param timeout 超时时间
     * @return
     */
    public ClientInitiativeProtocol<C> timeout(long timeout) {
        validateTimeout(timeout);
        return this;
    }

    protected String getMessageId() {
        ClientMessage message = requestMessage();
        return message.getMessageId();
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

    protected CountDownLatch getDownLatch() {
        return downLatch;
    }

    protected void setDownLatch(CountDownLatch downLatch) {
        this.downLatch = downLatch;
    }

    @Override
    public String relationKey() {
        return getMessageId();
    }

    public long getTimeout() {
        return timeout;
    }

    public FreeProtocolHandle getFreeProtocolHandle() {
        return freeProtocolHandle;
    }
}
