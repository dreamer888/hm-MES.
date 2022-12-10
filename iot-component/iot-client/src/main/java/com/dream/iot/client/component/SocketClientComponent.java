package com.dream.iot.client.component;

import com.dream.iot.*;
import com.dream.iot.client.*;
import com.dream.iot.client.protocol.ClientSocketProtocol;
import com.dream.iot.codec.SocketMessageDecoder;
import com.dream.iot.codec.filter.CombinedFilter;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.ReferenceCounted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;

/**
 * 基于socket的客户端组件 主要tcp, upd等
 * @param <M>
 */
public abstract class SocketClientComponent<M extends ClientMessage, R extends ReferenceCounted> extends ProtocolFactory<M>
        implements ClientComponent<M>, SocketMessageDecoder<R>, IotProtocolFactory<M>, InitializingBean {

    private long startTime;
    /**
     * 默认客户端
     * @see #getConfig()
     */
    private IotClient iotClient;
    private Class<M> messageClass;
    private Constructor<M> constructor;
    private IotThreadManager threadManager;
    private ClientConnectProperties config;
    private MultiClientManager clientManager;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public SocketClientComponent() {
        this(null);
    }

    /**
     * @param config 默认客户端
     */
    public SocketClientComponent(ClientConnectProperties config) {
        this(config, new SimpleMultiClientManager(null));
    }

    public SocketClientComponent(ClientConnectProperties config, MultiClientManager clientManager) {
        this.config = config;
        this.clientManager = clientManager;
        this.setDelegation(createProtocolTimeoutStorage());

        if(this.clientManager.getClientComponent() == null) {
            this.clientManager.setClientComponent(this);
        }

        getClient();  // 生成iotClient   lgl
    }

    @Override
    public void init(Object ...args) {
        if(getConfig() != null) {
            this.getClient().init(args[0]);
        }
    }

    @Override
    public void finished() {
        ClientComponent.super.finished();
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void connect() {
        if(this.getConfig() != null) {
            this.getClient().connect((future) -> {
                final ChannelFuture channelFuture = (ChannelFuture) future;
                channelFuture.addListener(future1 -> {
                    if(future1.isSuccess()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("默认客户端({}) 连接服务器成功 - 远程主机 {}:{} - 客户端标识：{}"
                                    , this.getName(), this.config.getHost(), this.config.getPort(), getConfig().connectKey());
                        }
                    }
                });
            }, 3000);
        }
    }

    @Override
    public void addClient(Object clientKey, IotClient value) {
        this.clientManager.addClient(clientKey, value);
    }

    @Override
    public IotClient getClient() {
        if(this.iotClient != null) {
            return this.iotClient;
        } else {
            if(this.getConfig() != null) {
                return this.iotClient = createNewClient(this.config);
            }
        }

        return null;
    }

    /**
     * 写出报文
     * @param properties 客户端配置
     * @param msg 发送的协议
     * @param args 自定义参数
     * @return
     */
    public ChannelFuture writeAndFlush(ClientConnectProperties properties, Object msg, Object... args) {
        SocketClient socketClient = this.getOrElseCreate(properties);
        return socketClient.writeAndFlush(msg, args);
    }

    /**
     * 写出协议
     * @see Protocol#requestMessage() 请求的报文
     * @see Protocol#responseMessage() 响应的报文
     * @param properties 客户端配置
     * @param protocol 要写出的协议
     * @return
     */
    public ChannelFuture writeAndFlush(ClientConnectProperties properties, ClientSocketProtocol protocol) {
        return this.writeAndFlush(properties, protocol, null);
    }

    @Override
    public List<IotClient> clients() {
        return clientManager.clients();
    }

    @Override
    public SocketClient getClient(Object clientKey) {
        return (SocketClient) clientManager.getClient(clientKey);
    }


    /**
     * 获取客户端 如果不存在则创建
     * @param properties
     * @return
     */
    public synchronized SocketClient getOrElseCreate(ClientConnectProperties properties) {
        SocketClient client = this.getClient(properties);
        if(client == null) {
            return this.createNewClientAndConnect(properties);
        }

        return client;
    }

    @Override
    public IotClient removeClient(Object clientKey) {
        return clientManager.removeClient(clientKey);
    }

    @Override
    public void setClientComponent(ClientComponent component) {
        this.clientManager.setClientComponent(component);
    }

    @Override
    public Class<M> getMessageClass() {
        if(this.messageClass == null) {
            // @since 2.3.0 只解析一次报文类
            this.messageClass = ClientComponent.super.getMessageClass();

            // 注意：只在空的时候解析一次
            this.resolveConstructor();
        }

        return this.messageClass;
    }

    @Override
    public SocketMessage createMessage(byte[] message) {
        final Constructor<M> constructor = this.resolveConstructor();
        return BeanUtils.instantiateClass(constructor, message);
    }

    private Constructor<M> resolveConstructor() {
        if(this.constructor == null) {
            try {
                this.constructor = this.getMessageClass().getConstructor(byte[].class);
            } catch (NoSuchMethodException e) {
                final String simpleName = this.getMessageClass().getSimpleName();
                throw new ProtocolException("报文类型缺少构造函数["+simpleName+"(byte[])]", e);
            }
        }

        return this.constructor;
    }

    /**
     * 创建一个新客户端
     * @param config
     * @return
     */
    public abstract SocketClient createNewClient(ClientConnectProperties config);

    /**
     * 创建新的客户端并连接
     * @param config
     */
    public SocketClient createNewClientAndConnect(ClientConnectProperties config) {
        SocketClient newClient = createNewClient(config);
        EventLoopGroup workerGroup = threadManager.getWorkerGroup();
        newClient.init((NioEventLoopGroup) workerGroup);
        // 创建连接
        newClient.connect((future) -> {
            final ChannelFuture channelFuture = (ChannelFuture) future;
            channelFuture.addListener(future1 -> {
                if(future1.isSuccess() && logger.isDebugEnabled()) {
                    logger.debug("客户端({}) 连接服务器成功 - 远程主机 {}:{} - 客户端标识：{}"
                            , this.getName(), config.getHost(), config.getPort(), config.connectKey());
                }
            });
        }, 3000).syncUninterruptibly();
        return newClient;
    }

    @Override
    public long startTime() {
        return startTime;
    }

    @Override
    public Optional<CombinedFilter> getFilter() {
        return FilterManager.getInstance().getFilter(getClass());
    }

    @Override
    public IotProtocolFactory protocolFactory() {
        return this;
    }

    public MultiClientManager getClientManager() {
        return clientManager;
    }

    protected ProtocolTimeoutStorage createProtocolTimeoutStorage() {
        return new ProtocolTimeoutStorage(getName());
    }

    public ClientConnectProperties getConfig() {
        return config;
    }

    public void setConfig(ClientConnectProperties config) {
        this.config = config;
    }

    @Autowired
    protected void setThreadManager(IotThreadManager threadManager) {
        this.threadManager = threadManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception { }
}
