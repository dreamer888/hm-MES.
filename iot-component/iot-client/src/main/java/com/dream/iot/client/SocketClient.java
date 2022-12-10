package com.dream.iot.client;

import com.dream.iot.IotThreadManager;
import com.dream.iot.ProtocolException;
import com.dream.iot.client.codec.ClientProtocolEncoder;
import com.dream.iot.client.component.SocketClientComponent;
import com.dream.iot.client.handle.ClientServiceHandler;
import com.dream.iot.client.protocol.ClientSocketProtocol;
import com.dream.iot.codec.adapter.SocketMessageDecoderDelegation;
import com.dream.iot.event.ClientStatus;
import com.dream.iot.event.ClientStatusEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.BlockingOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.dream.iot.CoreConst.*;

/**
 * client socket
 * @see TcpSocketClient
 * @see UdpSocketClient
 */
public abstract class SocketClient implements IotClient<NioEventLoopGroup> {

    private Channel channel;
    private Bootstrap bootstrap;
    /**
     * 重连时间
     */
    private long reconnectTime;
    /**
     * 重连次数
     */
    private int reconnectTimes;
    /**
     * 重连标记
     */
    private boolean reconnectFlag;
    private ClientConnectProperties config;
    private SocketClientComponent clientComponent;

    public Logger logger = LoggerFactory.getLogger(getClass());

    public SocketClient(SocketClientComponent clientComponent, ClientConnectProperties config) {
        this.config = config;
        this.clientComponent = clientComponent;
        this.reconnectTime = config.getReconnect();

        if(this.config == null) {
            throw new IllegalArgumentException("未指定连接配置[ConnectProperties]");
        }
    }

    public void init(NioEventLoopGroup clientGroup) {
        this.bootstrap = new Bootstrap().group(clientGroup)
                .channel(channel()).handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        // 关掉原有的链接
                        if(SocketClient.this.channel != null) {
                            SocketClient.this.channel.close();
                        }

                        SocketClient.this.channel = channel;
                        ChannelPipeline pipeline = channel.pipeline();

                        // 设置编解码器
                        ChannelInboundHandler decoder = createProtocolDecoder();
                        if(decoder instanceof SocketMessageDecoderDelegation) {
                            if(((SocketMessageDecoderDelegation<?>) decoder).getDelegation() == null) {
                                ((SocketMessageDecoderDelegation<?>) decoder).setDelegation(getClientComponent());
                            }
                        }

                        // 设置当前客户端连接的服务器配置信息
                        channel.attr(CLIENT_KEY).set(SocketClient.this.getConfig());

                        // 设置iot客户端编解码器
                        pipeline.addFirst(CLIENT_DECODER_HANDLER, decoder);
                        pipeline.addFirst(CLIENT_ENCODER_HANDLER, createProtocolEncoder());

                        // 业务处理器新增到最后
                        pipeline.addLast(CLIENT_SERVICE_HANDLER, new ClientServiceHandler(getClientComponent()));

                        // 自定义处理器
                        SocketClient.this.doInitChannel(channel);

                    }
                });

        this.doInitOptions(this.bootstrap);
    }

    @Override
    public ChannelFuture connect(Consumer<?> consumer, long timeout) {
        try {
            // 只有在未激活的情况下才进行连接
            if(this.getChannel() == null || !this.getChannel().isActive()) {
                return this.doConnect(consumer == null ? (future) -> {
                    if(future.isSuccess()) {
                        this.reconnectTimes = 0; // 恢复重连次数计数
                        this.reconnectTime = getConfig().getReconnect(); // 恢复重连时间
                        if (logger.isDebugEnabled()) {
                            logger.debug("客户端({}) 连接服务器成功 - 远程主机 {}:{} - 客户端标识：{}"
                                    , getClientComponent().getName(), this.getHost(), this.getPort(), getConfig().connectKey());
                        }
                    }
                } : (Consumer<ChannelFuture>) consumer, timeout);
            } else {
                return getChannel().newSucceededFuture();
            }
        } catch (Exception e) {
            return getChannel().newFailedFuture(new ProtocolException("连接失败 " + e.getMessage(), e));
        }
    }

    /**
     * 真正连接服务器的方法
     * @see MultiClientManager#addClient(Object, IotClient) <br>
     *     注意：连接成功后需要加入到客户端管理器
     *
     * @param consumer 可以自定义连接成功或的操作
     * @param timeout 指定连接超时时间
     */
    protected ChannelFuture doConnect(Consumer<ChannelFuture> consumer, long timeout) {
        return this.bootstrap.connect(getConfig().remoteSocketAddress(), getConfig().localSocketAddress()).addListener(future -> {
            try {
                consumer.accept((ChannelFuture) future);
            } finally {
                if (future.isSuccess()) {
                    // 连接成功必须保存, 否则会出现获取不到客户端的情况
                    // 新增客户端会判断此客户端是否已经存在的情况
                    clientComponent.addClient(getConfig(), this);

                    this.successCallback((ChannelFuture) future);
                } else {
                    // 此处重连需要符合其中某个条件 1. 客户端是默认客户端 2. 此客户端曾经连接成功过
                    if(clientComponent.getClient() == this || clientComponent.getClient(getConfig()) != null) {
                        this.reconnection();
                    }
                }
            }
        });
    }

    /**
     * 断开连接
     * @param remove true.直接移除客户端  false. 将会重连
     */
    public ChannelFuture disconnect(boolean remove) {
        return this.channel.disconnect().addListener(future -> {
            if(future.isSuccess()) {
                disconnectSuccessCall(remove); // 移除客户端处理
                if(logger.isDebugEnabled()) {
                    logger.debug("客户端({}) 关闭客户端(成功) - 远程地址：{}", getName(), getConfig());
                }
            } else {
                logger.error("客户端({}) 关闭客户端(失败) - 远程地址：{}", getName(), getConfig(), future.cause());
            }
        });
    }

    /**
     * 断线成功后的处理
     * 默认的处理方式：remove=true 移除 remove=false 重连
     */
    protected void disconnectSuccessCall(boolean remove) {
        if(remove) { // 移除
            getClientComponent().removeClient(getConfig());
        } else { // 重连
            reconnection();
        }
    }

    protected void doInitOptions(Bootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
    }

    protected abstract Class<? extends Channel> channel();

    /**
     * 连接配置
     * @return
     */
    public ClientConnectProperties getConfig() {
        return this.config;
    }

    @Override
    public int getPort() {
        return getConfig().getPort();
    }

    @Override
    public String getHost() {
        return getConfig().getHost();
    }

    protected void connectLogger(ChannelFuture future) {
        if (logger.isDebugEnabled() && future.isSuccess()) {
            logger.debug("客户端({}) 连接服务器成功 - 远程主机 {}:{}"
                    , getClientComponent().getName(), this.getHost(), this.getPort());
        } else if (!future.isSuccess()) {
            Throwable cause = future.cause();
            logger.error("客户端({}) 连接服务器失败 - 远程主机 {}:{}", getClientComponent().getName()
                    , getConfig().getReconnect(), this.getHost(), this.getPort(), cause);
        }
    }

    /**
     * 断线重连
     * @see ClientConnectProperties#getReconnect() 重连时间
     */
    public synchronized void reconnection() {
        // 指定了重连时间并且客户端未激活
        // 重连五次
        if(!this.reconnectFlag && this.reconnectTimes < 5 && this.reconnectTime > 0 && !this.getChannel().isActive()) {
            this.reconnectFlag = true; this.reconnectTimes ++;
            logger.warn("客户端({}) 断线重连(第{}次) - 等待重连时间：{}(s) - 远程主机 {}:{} - 客户端标识：{}"
                    , getClientComponent().getName(), this.reconnectTimes, reconnectTime, this.getHost(), this.getPort(), getConfig().connectKey());
            IotThreadManager.instance().getDeviceManageEventExecutor().schedule(() -> {
                this.connect((future) -> {
                    final ChannelFuture channelFuture = (ChannelFuture) future;
                    if(channelFuture.isSuccess()) {
                        this.reconnectTimes = 0;
                        this.reconnectTime = this.getConfig().getReconnect();
                        logger.info("客户端({}) 重连成功 - 远程主机 {}:{} - 客户端标识：{}", getClientComponent().getName()
                                , this.getHost(), this.getPort(), getConfig().connectKey());
                    } else {
                        // 连接失败, 增加下一次重连的时间
                        this.reconnectTime = this.reconnectTime + this.reconnectTime * this.reconnectTimes;
                        logger.error("客户端({}) 重连失败 - 远程主机 {}:{} - 客户端标识：{}", getClientComponent().getName()
                                , this.getHost(), this.getPort(), getConfig().connectKey(), channelFuture.cause());
                    }

                    this.reconnectFlag = false;
                }, 3000);
            }, this.reconnectTime, TimeUnit.SECONDS);
        }
    }

    /**
     * 写出协议报文
     * @see ClientProtocolEncoder 协议编码器, 此处是真正写出报文的地方
     * @param clientProtocol
     * @return
     */
    public ChannelFuture writeAndFlush(ClientSocketProtocol clientProtocol) {
        return this.writeAndFlush(clientProtocol, null);
    }

    public ChannelFuture writeAndFlush(Object msg, Object... args) {
        if(this.getChannel() == null || !this.getChannel().isActive()) {
            try {
                // 尝试重连, 并且等待
                return connect((arg) -> {
                    ChannelFuture future = (ChannelFuture) arg;
                    if(future.isSuccess() && logger.isDebugEnabled()) {
                        logger.debug("客户端({}) 写报文重连成功 - 远程主机 {}:{} - 客户端标识：{}"
                                , getClientComponent().getName(), this.getHost(), this.getPort(), getConfig().connectKey());
                    }
                }, 3000).addListener((future) -> {
                    final ChannelFuture channelFuture = (ChannelFuture) future;
                    // 重连成功后写出报文
                    if(channelFuture.isSuccess()) {
                        this.writeAndFlush(msg);
                    }
                }).syncUninterruptibly();
            } catch (BlockingOperationException e) {
                throw new ProtocolException("写报文尝试重连失败 连接同步死锁", e);
            } catch (Exception e) {
                throw new ProtocolException("写报文尝试重连失败 " + e.getMessage() , e);
            }
        } else if(this.getChannel().isWritable()){
            return this.getChannel().writeAndFlush(msg);
        } else {
            return this.getChannel().newFailedFuture(new UnWritableProtocolException(msg));
        }
    }

    protected void doInitChannel(Channel channel) { }

    /**
     * 连接成功后的回调
     * @param future
     */
    protected void successCallback(ChannelFuture future) {
        // 发布客户端上线事件
        IotClientBootstrap.publishApplicationEvent(new ClientStatusEvent(this, ClientStatus.online, getClientComponent()));
    }

    /**
     * 创建客户端socket解码器
     * @see io.netty.handler.codec.ByteToMessageDecoder 此对象的子类不能使用 {@link io.netty.channel.ChannelHandler.Sharable}
     * @return
     */
    protected abstract ChannelInboundHandler createProtocolDecoder();

    /**
     * 创建socket编码器
     * @return 默认使用iot框架实现的编码器 {@link ClientProtocolEncoder}
     */
    protected ChannelOutboundHandlerAdapter createProtocolEncoder() {
        return new ClientProtocolEncoder(getClientComponent());
    }

    public SocketClientComponent getClientComponent() {
        return clientComponent;
    }

    public void setClientComponent(SocketClientComponent clientComponent) {
        this.clientComponent = clientComponent;
    }

    public String getName() {
        return this.clientComponent.getName();
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
