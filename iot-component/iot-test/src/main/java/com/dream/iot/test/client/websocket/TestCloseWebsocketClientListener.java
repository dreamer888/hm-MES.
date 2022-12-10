package com.dream.iot.test.client.websocket;

import com.dream.iot.client.websocket.impl.DefaultWebSocketClientProtocol;
import com.dream.iot.client.websocket.WebSocketClientListener;
import com.dream.iot.server.IotServerProperties;
import com.dream.iot.test.IotTestProperties;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 关闭帧测试
 */
@Component
@ConditionalOnProperty(prefix = "iot.server", name = "websocket.start", havingValue = "true")
public class TestCloseWebsocketClientListener implements WebSocketClientListener {

    @Autowired
    private IotTestProperties properties;

    @Autowired
    private IotServerProperties serverProperties;

    @Override
    public String uri() {
        String host = properties.getHost();
        IotServerProperties.WebSocketConnectProperties websocket = serverProperties.getWebsocket();
        return String.format("ws://%s:%d/close", host, websocket.getPort());
    }

    @Override
    public void onConnect(DefaultWebSocketClientProtocol protocol) {
        // 链接上之后直接关闭
        protocol.response(WebSocketCloseStatus.NORMAL_CLOSURE, "测试关闭");
    }

    @Override
    public void onText(DefaultWebSocketClientProtocol protocol) {

    }

    @Override
    public void onClose(DefaultWebSocketClientProtocol protocol) {

    }

    @Override
    public void onBinary(DefaultWebSocketClientProtocol protocol) {

    }
}
