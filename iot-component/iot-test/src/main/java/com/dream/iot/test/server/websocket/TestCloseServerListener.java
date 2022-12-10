package com.dream.iot.test.server.websocket;

import com.dream.iot.server.websocket.impl.DefaultWebSocketServerProtocol;
import com.dream.iot.server.websocket.WebSocketServerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "iot.server", name = "websocket.start", havingValue = "true")
public class TestCloseServerListener implements WebSocketServerListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String uri() {
        return "/close";
    }

    @Override
    public void onText(DefaultWebSocketServerProtocol protocol) {

    }

    @Override
    public void onClose(DefaultWebSocketServerProtocol protocol) {
        logger.info("客户端关闭测试：" + protocol.getEquipCode());
    }

    @Override
    public void onBinary(DefaultWebSocketServerProtocol protocol) {

    }
}
