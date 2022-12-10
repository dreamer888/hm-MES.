package com.dream.iot.client;

import com.dream.iot.*;
import com.dream.iot.business.BusinessFactory;
import com.dream.iot.client.http.HttpManager;
import com.dream.iot.client.http.okhttp.OkHttpManager;
import com.dream.iot.client.proxy.ProxyClientComponent;
import com.dream.iot.client.websocket.impl.DefaultWebSocketClientComponent;
import com.dream.iot.client.websocket.impl.DefaultWebSocketClientProtocolHandle;
import com.dream.iot.client.websocket.impl.DefaultWebSocketListenerManager;
import com.dream.iot.client.websocket.WebSocketClientListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.stream.Collectors;

@Order(88168)
@AutoConfigureAfter(IotCoreConfiguration.class)
@EnableConfigurationProperties(ClientProperties.class)
public class IotClientBootstrap implements InitializingBean, ApplicationListener<ApplicationStartedEvent>, ApplicationContextAware {

    public static BusinessFactory businessFactory;
    public static ApplicationContext applicationContext;
    public static ClientComponentFactory clientComponentFactory;

    private ClientProperties properties;
    public static HttpManager httpManager;
    public static DeviceManager deviceManager;
    protected IotThreadManager threadManager;

    public IotClientBootstrap(IotThreadManager threadManager, ClientProperties properties) {
        this.properties = properties;
        this.threadManager = threadManager;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent contextRefreshedEvent) {
        /**
         * 只有在将客户端和服务端在同一个应用上下文的时候才会有设备管理
         */
        if(applicationContext.containsBean("deviceManager")) {
            IotClientBootstrap.deviceManager = applicationContext.getBean(DeviceManager.class);
        }

        // 连接组件默认的客户端
        clientComponentFactory.getComponents().forEach(item -> item.connect());

        // 连接完成回调
        clientComponentFactory.getComponents().forEach(item -> item.finished());
    }

    @Override
    public void afterPropertiesSet() throws Exception { }

    public static <T extends IotClient> T getClient(Class<? extends ClientMessage> clazz, Object clientKey) {
        ClientComponent clientComponent = clientComponentFactory.getByClass(clazz);

        /**
         * 通过报文类型{@link ClientMessage}获取唯一对应的组件{@link ClientComponent#getMessageClass()}
         * 且必须注册到spring容器
         */
        if(clientComponent == null) {
            throw new IllegalArgumentException("未注册与报文类型[" + clazz.getSimpleName()
                    +"]对应的客户端组件[" + ClientComponent.class.getSimpleName() + "]" );
        }

        if(null == clientKey) {
            // 使用默认的客户端
            return (T) clientComponent.getClient();
        } else {
            // 使用指定的客户端
            return (T) clientComponent.getClient(clientKey);
        }
    }

    public static <T> T getBean(Class<T> requiredClass) {
        return applicationContext.getBean(requiredClass);
    }

//    @Bean("httpManager")
//    @ConditionalOnMissingBean(HttpManager.class)
    public HttpManager httpManager() {
        return new OkHttpManager();
    }

    /**
     * 返回客户端组件工厂
     * @return
     */
    public static ClientComponentFactory getClientComponentFactory() {
        return clientComponentFactory;
    }

    public static ClientComponent getClientComponent(Class<? extends ClientMessage> clazz) {
        return getClientComponentFactory().getByClass(clazz);
    }

    public static IotProtocolFactory getProtocolFactory(Class<? extends ClientMessage> clazz) {
        return clientComponentFactory.getByClass(clazz).protocolFactory();
    }

    /**
     * 发布应用事件
     * @param event
     */
    public static void publishApplicationEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        IotClientBootstrap.applicationContext = applicationContext;
    }

    @Bean
    public ClientProtocolTimeoutManager clientProtocolTimeoutManager(ClientComponentFactory factory) {
        List<ProtocolTimeoutStorage> timeoutStorages = factory.getComponents().stream()
                .filter(item -> item.protocolFactory() instanceof ProtocolFactory)
                .map(item -> ((ProtocolFactory<?>) item.protocolFactory()).getDelegation())
                .collect(Collectors.toList());

        return new ClientProtocolTimeoutManager(timeoutStorages);
    }

    @Bean
    public ClientProtocolHandleFactory clientBusinessFactory() {
        IotClientBootstrap.businessFactory = new ClientProtocolHandleFactory();
        return (ClientProtocolHandleFactory) IotClientBootstrap.businessFactory;
    }

    @Bean
    public ClientComponentFactory clientComponentFactory(IotThreadManager threadManager, ObjectProvider<List<ClientComponent>> provider) {
        return clientComponentFactory = new ClientComponentFactory(threadManager, provider.getIfAvailable());
    }

    @Bean
    @ConditionalOnExpression("${iot.client.proxy.start:false}")
    public ProxyClientComponent proxyClientComponent() {
        return new ProxyClientComponent(properties.getProxy());
    }

    @Bean
    @ConditionalOnBean(WebSocketClientListener.class)
    public DefaultWebSocketClientComponent defaultWebSocketClientComponent() {
        return new DefaultWebSocketClientComponent();
    }

    @Bean
    @ConditionalOnBean(WebSocketClientListener.class)
    public DefaultWebSocketClientProtocolHandle defaultWebSocketClientProtocolHandle(DefaultWebSocketClientComponent clientComponent) {
        return new DefaultWebSocketClientProtocolHandle(clientComponent);
    }

    @Bean
    @ConditionalOnBean(WebSocketClientListener.class)
    public DefaultWebSocketListenerManager defaultWebSocketListenerManager(List<WebSocketClientListener> listeners) {
        return new DefaultWebSocketListenerManager(listeners);
    }
}
