package com.dream.iot.client.websocket.impl;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.SocketMessage;
import com.dream.iot.client.*;
import com.dream.iot.client.codec.WebSocketClient;
import com.dream.iot.client.websocket.WebSocketClientComponentAbstract;
import com.dream.iot.client.websocket.WebSocketClientConnectProperties;
import com.dream.iot.client.websocket.WebSocketClientListener;
import com.dream.iot.websocket.WebSocketException;

import java.net.URI;

public class DefaultWebSocketClientComponent extends WebSocketClientComponentAbstract<DefaultWebSocketClientMessage> {

    private DefaultWebSocketListenerManager listenerManager;

    public DefaultWebSocketClientComponent() { }

    public DefaultWebSocketClientComponent(MultiClientManager clientManager) {
        super(null, clientManager);
    }

    @Override
    public TcpSocketClient createNewClient(ClientConnectProperties config) {
        return new WebSocketClient(this, (WebSocketClientConnectProperties) config);
    }

    @Override
    public String getName() {
        return "websocket(Client)";
    }

    @Override
    public String getDesc() {
        return "WebSocket客户端默认实现";
    }

    @Override
    public WebSocketClientConnectProperties getConfig() {
        return (WebSocketClientConnectProperties) super.getConfig();
    }

    @Override
    public SocketMessage createMessage(byte[] message) {
        return new DefaultWebSocketClientMessage(message);
    }

    @Override
    public AbstractProtocol getProtocol(DefaultWebSocketClientMessage message) {
        return new DefaultWebSocketClientProtocol(message);
    }

    @Override
    public SocketClient createNewClientAndConnect(ClientConnectProperties config) {
        if(config instanceof WebSocketClientConnectProperties) {
            if(((WebSocketClientConnectProperties) config).getListener() == null) {
                URI uri = ((WebSocketClientConnectProperties) config).getUri();
                // 查找可以处理此uri的监听器
                WebSocketClientListener listener = listenerManager.getListenerByURI(uri);
                if(listener != null) {
                    ((WebSocketClientConnectProperties) config).bindListener(listener);
                } else {
                    logger.warn("uri["+uri.getAuthority()+uri.getPath()+"]没有匹配到监听器");
                }
            }
        } else {
            throw new WebSocketException("websocket协议请使用配置类型["+WebSocketClientConnectProperties.class.getSimpleName()+"]");
        }
        return super.createNewClientAndConnect(config);
    }

    @Override
    public void connect() {
        super.connect();
        IotClientBootstrap.applicationContext.getAutowireCapableBeanFactory()
                .getBeanProvider(DefaultWebSocketListenerManager.class).ifAvailable(manager -> {
            listenerManager = manager;

            //创建所有websocket客户端
            manager.getListenerConfig().forEach(properties -> {
                createNewClientAndConnect(properties);
            });
        });
    }
}
