package com.dream.iot.client.websocket.impl;

import com.dream.iot.client.websocket.WebSocketClientConnectProperties;
import com.dream.iot.client.websocket.WebSocketClientListener;
import com.dream.iot.websocket.WebSocketException;
import org.springframework.beans.factory.InitializingBean;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultWebSocketListenerManager implements InitializingBean {

    private List<WebSocketClientListener> listeners;
    private List<WebSocketClientConnectProperties> listenerConfig = new ArrayList<>();
    private Map<String, WebSocketClientListener> uriListenerMap = new HashMap<>(16);

    public DefaultWebSocketListenerManager(List<WebSocketClientListener> listeners) {
        this.listeners = listeners;
    }

    public List<WebSocketClientListener> getListeners() {
        return listeners;
    }

    public WebSocketClientListener getListenerByUri(String uri) {
        return uriListenerMap.get(uri);
    }

    public WebSocketClientListener getListenerByURI(URI uri) {
        return uriListenerMap.get(uri.getAuthority() + uri.getPath());
    }

    public List<WebSocketClientConnectProperties> getListenerConfig() {
        return listenerConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 连接所有需要监听的客户端
        this.getListeners().forEach(item -> {
            WebSocketClientConnectProperties properties = item.properties();

            String path = properties.getUri().getPath();
            String authority = properties.getUri().getAuthority();

            WebSocketClientListener listener = uriListenerMap.get(authority + path);
            if(listener == null) {
                properties.bindListener(item);
                listenerConfig.add(properties);
                uriListenerMap.put(authority + path, item);
            } else {
                throw new WebSocketException("uri["+authority+path+"]已经注册了监听器["+listener.getClass().getSimpleName()+"]");
            }
        });
    }
}
