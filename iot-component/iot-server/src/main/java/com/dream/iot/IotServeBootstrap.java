package com.dream.iot;

import com.dream.iot.business.BusinessFactory;
import com.dream.iot.client.ClientHandleFactory;
import com.dream.iot.client.ClientProxyServerComponent;
import com.dream.iot.client.ClientProxyServerHandle;
import com.dream.iot.client.handle.ClientHandleBeanPostProcessor;
import com.dream.iot.codec.adapter.SocketMessageDecoderDelegation;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.*;
import com.dream.iot.server.codec.DeviceProtocolEncoder;
import com.dream.iot.server.codec.UdpServerProtocolEncoder;
import com.dream.iot.server.endpoints.ServerHealthWebsocketEndpoint;
import com.dream.iot.server.handle.EventManagerHandler;
import com.dream.iot.server.handle.ProtocolBusinessHandler;
import com.dream.iot.server.udp.UdpServerComponent;
import com.dream.iot.server.udp.impl.DefaultUdpConnectProperties;
import com.dream.iot.server.udp.impl.DefaultUdpServerComponent;
import com.dream.iot.server.websocket.impl.DefaultWebSocketServerProtocolHandle;
import com.dream.iot.server.websocket.impl.DefaultWebSocketServerComponent;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.dream.iot.CoreConst.*;

/**
 * Create Date By 2017-09-06
 *  <h4>设备网络通信程序启动入口</h4>
 * @author teaj
 * @since 1.7
 */
@Order(88158)
@AutoConfigureAfter(IotCoreConfiguration.class)
@EnableConfigurationProperties({IotServerProperties.class, DefaultUdpConnectProperties.class})
public class IotServeBootstrap implements InitializingBean
        , BeanFactoryAware, ApplicationListener<ApplicationStartedEvent> {

    private final IotCoreProperties coreProperties;
    public static BeanFactory BEAN_FACTORY; //spring bean factory

    private ServerBootstrap tcpBootstrap;
    private IotThreadManager threadManager;
    public DeviceProtocolEncoder protocolEncoder;
    private UdpServerProtocolEncoder udpProtocolEncoder;
    private ProtocolBusinessHandler protocolHandler;


    protected static ApplicationContext applicationContext;
    public static ServerComponentFactory COMPONENT_FACTORY;
    public static ServerProtocolHandleFactory BUSINESS_FACTORY;
    @Autowired
    private ServerBootstrapInitializing serverBootstrapInitializing;

    private static Logger logger = LoggerFactory.getLogger(IotServeBootstrap.class);

    public IotServeBootstrap(IotCoreProperties coreProperties) {
        this.coreProperties = coreProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        threadManager = BEAN_FACTORY.getBean(IotThreadManager.class);
        protocolHandler = BEAN_FACTORY.getBean(ProtocolBusinessHandler.class);
        protocolEncoder = BEAN_FACTORY.getBean(DeviceProtocolEncoder.class);
        COMPONENT_FACTORY = BEAN_FACTORY.getBean(ServerComponentFactory.class);

        //初始化Netty服务
        initNettyServer();
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        IotServeBootstrap.applicationContext = event.getApplicationContext();

        try {
            IotServeBootstrap.BUSINESS_FACTORY = IotServeBootstrap.applicationContext.getBean(ServerProtocolHandleFactory.class);
            if(null == BUSINESS_FACTORY) {
                throw new IllegalArgumentException("找不到业务工厂：" + ServerProtocolHandleFactory.class.getName());
            }

            doBind(applicationContext);
        } catch (Exception exception) {

            // 异常关闭应用上下文
            if(applicationContext instanceof ConfigurableApplicationContext) {
                ((ConfigurableApplicationContext) applicationContext).close();
            }
        }
    }

    /**
     * 开启netty服务器
     */
    protected IotServeBootstrap initNettyServer() {
        // 初始化tcp服务
        final List<TcpServerComponent> serverComponents = COMPONENT_FACTORY.getTcpserverComponents();

        if(!CollectionUtils.isEmpty(serverComponents)) {
            initTcpServe();

            // 所有组件初始化完成
            serverComponents.forEach(item -> item.finished());
        }

        return this;
    }

    protected void initTcpServe() {
        tcpBootstrap = new ServerBootstrap().group(threadManager.getBossGroup(), threadManager.getWorkerGroup())
                .handler(new LoggingHandler(coreProperties.getLevel()));
        this.serverBootstrapInitializing.tcp(tcpBootstrap);
        tcpBootstrap.channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();

                        int port = ch.parent().localAddress().getPort();
                        IotServeBootstrap.this.doSocketChannelInitializer(ch, p, port);
                    }
                });
    }

    protected void doSocketChannelInitializer(SocketChannel ch, ChannelPipeline p, int port) {
        SocketServerComponent serverComponent = COMPONENT_FACTORY.getByPort(port);
        if(serverComponent instanceof TcpServerComponent) {
            IotSocketServer iotSocketServer = serverComponent.deviceServer();
            if(iotSocketServer instanceof BeanFactoryAware) {
                ((BeanFactoryAware) iotSocketServer).setBeanFactory(BEAN_FACTORY);
            }

            // 设置编码器
            p.addFirst(SERVER_ENCODER_HANDLER, protocolEncoder);

            // 设置解码器
            final ChannelInboundHandlerAdapter decoder = iotSocketServer.getMessageDecoder();
            if(decoder == null) {
                throw new ProtocolException("未指定设备报文解码器：" + serverComponent.getName());
            }

            if(decoder instanceof SocketMessageDecoderDelegation) {
                if(((SocketMessageDecoderDelegation<?>) decoder).getDelegation() == null) {
                    ((SocketMessageDecoderDelegation<?>) decoder).setDelegation(serverComponent);
                }
            }

            p.addFirst(SERVER_DECODER_HANDLER, decoder);

            // 有一个值设定, 就启用
            final ConnectProperties config = iotSocketServer.config();
            if(config.getReaderIdleTime() > 0 || config.getAllIdleTime() > 0
                    || config.getWriterIdleTime() > 0) {
                p.addLast(IDLE_STATE_EVENT_HANDLER, new IdleStateHandler(config.getReaderIdleTime()
                        , config.getWriterIdleTime(), config.getAllIdleTime(), TimeUnit.SECONDS));
            }

            // 新增事件管理处理
            final EventManagerHandler instance = EventManagerHandler.getInstance(COMPONENT_FACTORY);
            if(p.get(IDLE_STATE_EVENT_HANDLER) != null) { // 如果有启用超时处理
                p.addAfter(IDLE_STATE_EVENT_HANDLER, EVENT_MANAGER_HANDLER, instance);
            } else {
                p.addLast(EVENT_MANAGER_HANDLER, instance);
            }

            // 最后设置业务处理器
            p.addLast(SERVER_SERVICE_HANDLER, protocolHandler);

            // 自定义处理器
            serverComponent.init(p, ch);
        } else {
            logger.error("查无与端口: {}匹配的服务组件: {}, 所有与此端口连接的设备都无法处理", port, TcpServerComponent.class.getSimpleName());
        }
    }

    protected void doBind(final ApplicationContext context){
        // 监听TCP端口
        COMPONENT_FACTORY.getTcpserverComponents().forEach(item -> item.deviceServer().doBind(tcpBootstrap, context));

        // 监听UDP端口
        COMPONENT_FACTORY.getUdpServerComponents().forEach(item -> item.deviceServer().doBind(getUdpBootstrap(item), context));
    }

    private AbstractBootstrap getUdpBootstrap(UdpServerComponent serverComponent) {
        Bootstrap handler = new Bootstrap().group(threadManager.getWorkerGroup()).channel(NioDatagramChannel.class);
        this.serverBootstrapInitializing.udp(handler);
        return handler.handler(new ChannelInitializer<NioDatagramChannel>() {
            @Override
            protected void initChannel(NioDatagramChannel ch) throws Exception {
                IotSocketServer iotSocketServer = serverComponent.deviceServer();

                // 设置编码器
                ch.pipeline().addFirst(SERVER_ENCODER_HANDLER, udpProtocolEncoder);

                // 新增解码器到处理链第一个位置
                final ChannelInboundHandlerAdapter decoder = iotSocketServer.getMessageDecoder();
                if (decoder == null) {
                    throw new ProtocolException("未指定设备报文解码器：" + serverComponent.getName());
                }

                serverComponent.setChannel(ch);

                if(decoder instanceof SocketMessageDecoderDelegation) {
                    if(((SocketMessageDecoderDelegation<?>) decoder).getDelegation() == null) {
                        ((SocketMessageDecoderDelegation<?>) decoder).setDelegation(serverComponent);
                    }
                }

                ch.pipeline().addFirst(SERVER_DECODER_HANDLER, decoder);

                // 新增业务处理器到处理链到最后一个位置
                ch.pipeline().addLast(SERVER_SERVICE_HANDLER, new ProtocolBusinessHandler(COMPONENT_FACTORY, BUSINESS_FACTORY));

                // 自定义处理器
                serverComponent.init(ch.pipeline(), ch);
            }
        });
    }

    public static void publishApplicationEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    public static <T> T getBean(Class<T> requiredClass) {
        return BEAN_FACTORY.getBean(requiredClass);
    }

    public static SocketServerComponent getServerComponent(Class<? extends SocketMessage> messageClass) {
        return COMPONENT_FACTORY.getByClass(messageClass);
    }

    public static SocketServerComponent getServerComponent(int port) {
        return COMPONENT_FACTORY.getByPort(port);
    }

    public static BusinessFactory getBusinessFactory() {
        return IotServeBootstrap.BUSINESS_FACTORY;
    }

    @Bean
    public ServerProtocolHandleFactory deviceRequestBusinessFactory() {
        return new ServerProtocolHandleFactory();
    }

    @Bean
    public ProtocolBusinessHandler businessHandler(ServerComponentFactory componentFactory
            , ServerProtocolHandleFactory handleFactory) {
        return new ProtocolBusinessHandler(componentFactory, handleFactory);
    }

    /**
     * 服务组件工厂
     * @return
     */
    @Bean
    public ServerComponentFactory serverComponentFactory() {
        return new ServerComponentFactory();
    }

    /**
     * 设备协议编码器
     * 此编码器用于所有的设备
     * @see ChannelHandler.Sharable 单例处理器
     * @see SocketMessage#getMessage()
     * @param componentFactory
     * @return
     */
    @Bean
    public DeviceProtocolEncoder deviceProtocolEncoder(ServerComponentFactory componentFactory) {
        return new DeviceProtocolEncoder(componentFactory);
    }

    /**
     * udp协议使用的编码器
     * @param componentFactory
     * @return
     */
    @Bean
    public UdpServerProtocolEncoder udpProtocolEncoder(ServerComponentFactory componentFactory) {
        return this.udpProtocolEncoder = new UdpServerProtocolEncoder(componentFactory);
    }

    @Bean
    @ConditionalOnProperty(prefix = "iot.server.udp.default", name = "start", havingValue = "true")
    public DefaultUdpServerComponent defaultUdpServerComponent(DefaultUdpConnectProperties properties) {
        return new DefaultUdpServerComponent(properties);
    }

    /**
     * 用于监听应用程序客户端链接的服务端组件, 默认启用
     * @return
     */
    @Bean
    @ConditionalOnExpression("${iot.server.proxy.start:true}")
    public ClientProxyServerComponent appServerComponent(IotServerProperties properties) {
        return new ClientProxyServerComponent(properties.getProxy());
    }

    @Bean
    @ConditionalOnBean(ClientProxyServerComponent.class)
    public ClientProxyServerHandle appClientServerHandle(ObjectProvider<ClientHandleFactory> handleFactories) {
        return new ClientProxyServerHandle(handleFactories);
    }

    @Bean
    @ConditionalOnBean(ClientProxyServerComponent.class)
    public ClientHandleBeanPostProcessor clientHandleFactory() {
        return new ClientHandleBeanPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean(ServerBootstrapInitializing.class)
    public ServerBootstrapInitializing serverBootstrapInitializing() {
        return new DefaultServerBootstrapInitializing();
    }

    /**
     * 设备服务端协议的超时管理器
     * @param componentFactory
     * @return
     */
    @Bean("serverTimeoutProtocolManager")
    @ConditionalOnMissingBean(name = "serverTimeoutProtocolManager")
    public ServerTimeoutProtocolManager serverTimeoutProtocolManager(ServerComponentFactory componentFactory) {
        List<ProtocolTimeoutStorage> storages = componentFactory.getTcpserverComponents()
                .stream()
                .filter(item->item.protocolTimeoutStorage() != null)
                .map(item -> item.protocolTimeoutStorage())
                .collect(Collectors.toList());

        return new ServerTimeoutProtocolManager(storages);
    }

    @Bean
    @ConditionalOnProperty(prefix = "iot.server", name = "websocket.start", havingValue = "true")
    public DefaultWebSocketServerComponent defaultWebSocketServerComponent(IotServerProperties properties) {
        return new DefaultWebSocketServerComponent(properties.getWebsocket());
    }

    @Bean
    @ConditionalOnBean(DefaultWebSocketServerComponent.class)
    public DefaultWebSocketServerProtocolHandle defaultWebSocketProtocolHandle() {
        return new DefaultWebSocketServerProtocolHandle();
    }

    @Bean
    @ConditionalOnBean(DefaultWebSocketServerComponent.class)
    public ServerHealthWebsocketEndpoint serverHealthWebsocketEndpoint(ServerComponentFactory factory) {
        return new ServerHealthWebsocketEndpoint(factory);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        IotServeBootstrap.BEAN_FACTORY = beanFactory;
    }

}
