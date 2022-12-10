package com.dream.iot.server;

import com.dream.iot.SocketMessage;
import com.dream.iot.server.udp.UdpServerComponent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用来存储和获取设备组件{@link SocketServerComponent}
 * @see #getByPort(Integer) 通过端口获取
 * @see #getByClass(Class) 通过报文类型{@link SocketMessage}获取
 * @see #getTcpserverComponents() 获取TCP端口的设备组件列表
 * @see #getUdpServerComponents() 获取UDP端口的设备组件列表
 */
public class ServerComponentFactory implements InitializingBean, BeanFactoryAware {

    private BeanFactory beanFactory;
    private List<TcpServerComponent> tcpserverComponents = new ArrayList<>();
    private List<UdpServerComponent> udpServerComponents = new ArrayList<>();
    private Map<Integer, SocketServerComponent> componentMap = new HashMap<>(8);
    private Map<Class<? extends SocketMessage>, SocketServerComponent> messageComponentMap = new HashMap(8);

    public List<TcpServerComponent> getTcpserverComponents() {
        return this.tcpserverComponents;
    }

    public List<UdpServerComponent> getUdpServerComponents() {
        return this.udpServerComponents;
    }

    public SocketServerComponent getByPort(Integer port) {
        return componentMap.get(port);
    }

    public SocketServerComponent getByClass(Class<? extends SocketMessage> messageClass) {
        return messageComponentMap.get(messageClass);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final ObjectProvider<SocketServerComponent> beanProvider = this.beanFactory.getBeanProvider(SocketServerComponent.class);

        beanProvider.stream().forEach(component -> {
            IotSocketServer deviceServer = component.deviceServer();
            if(null == deviceServer) {
                throw new IllegalArgumentException("未指定设备服务对象: DeviceServerComponent.deviceServer()");
            }

            if(component instanceof TcpServerComponent) {
                tcpserverComponents.add((TcpServerComponent) component);
            }

            if(component instanceof UdpServerComponent) {
                udpServerComponents.add((UdpServerComponent) component);
            }

            int port = deviceServer.port();
            if(port <= 0 || port > 65535)
                throw new BeanInitializationException("服务端组件: " + component.getName() + "使用错误的端口: " + port);

            // 已经有组件使用此端口, 抛出异常
            final SocketServerComponent serverComponent = componentMap.get(port);
            if(serverComponent != null) {
                throw new BeanInitializationException(serverComponent.getName()
                        + "和" + component.getName() + "使用同一个端口: " + deviceServer.port());
            }

            // 每种设备组件的报文类型不能为空, 且不能和其他的设备组件的报文相同
            Class<? extends SocketMessage> messageClass = component.getMessageClass();
            if(messageClass == null) {
                throw new BeanInitializationException("请指定组件的报文类型："
                        + component.getClass().getSimpleName()+"<M extend ServerMessage>");
            }

            componentMap.put(port, component);
            messageComponentMap.put(messageClass, component);
        });
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
