package com.dream.iot.server.websocket.impl;

import com.dream.iot.server.ServerProtocolHandle;
import com.dream.iot.server.websocket.WebSocketServerListener;
import com.dream.iot.websocket.WebSocketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;

public class DefaultWebSocketServerProtocolHandle implements ServerProtocolHandle<DefaultWebSocketServerProtocol>, InitializingBean, BeanFactoryAware {

    private BeanFactory beanFactory;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, WebSocketServerListener> listeners = new HashMap<>();

    @Override
    public Object handle(DefaultWebSocketServerProtocol protocol) {
        DefaultWebSocketServerMessage requestMessage = protocol.requestMessage();
        WebSocketServerListener listener = listeners.get(requestMessage.uri());
        if(listener != null) {
            switch (requestMessage.frameType()) {
                case Text:
                    listener.onText(protocol); break;
                case Binary:
                    listener.onBinary(protocol); break;
                case Close:
                    listener.onClose(protocol); break;
                default: throw new WebSocketException("不支持的事件["+requestMessage.frameType()+"]");
            }
        } else {
            this.logger.warn("WebSocket 未找到监听器(请确认uri是否完全匹配) - uri：{} - cause：将导致无法处理此uri的请求", requestMessage.uri());
        }

        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.beanFactory.getBeanProvider(WebSocketServerListener.class).forEach(item -> {
            WebSocketServerListener listener = listeners.get(item.uri());
            if(listener != null) {
                // 监听器不支持一对多
                throw new BeanInitializationException("此uri["+item.uri()+"]已经配置WebSocket监听器["+listener.getClass().getSimpleName()+"]");
            }

            listeners.put(item.uri(), item);
        });

        if(this.listeners.isEmpty()) {
            this.logger.warn("WebSocket默认组件未指定任何监听器["+ WebSocketServerListener.class.getSimpleName()+"], 这将导致没办法处理任何客户端请求");
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
