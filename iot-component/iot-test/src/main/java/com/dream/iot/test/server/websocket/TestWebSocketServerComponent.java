package com.dream.iot.test.server.websocket;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.server.websocket.WebSocketServerComponentAbstract;
import com.dream.iot.test.IotTestProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "iot.test", name = "websocket.start", havingValue = "true")
public class TestWebSocketServerComponent extends WebSocketServerComponentAbstract<TestWebSocketMessage> {

    public TestWebSocketServerComponent(IotTestProperties properties) {
        super(properties.getWebsocket());
    }

    @Override
    public String getDesc() {
        return "Websocket服务端测试";
    }

    @Override
    public String getName() {
        return "websocket";
    }

    @Override
    public AbstractProtocol getProtocol(TestWebSocketMessage message) {
        return null;
    }
}
