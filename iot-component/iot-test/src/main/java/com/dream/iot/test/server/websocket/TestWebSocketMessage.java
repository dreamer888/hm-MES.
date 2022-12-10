package com.dream.iot.test.server.websocket;

import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.server.websocket.WebSocketServerMessageAbstract;
import com.dream.iot.test.TestProtocolType;

public class TestWebSocketMessage extends WebSocketServerMessageAbstract {

    public TestWebSocketMessage(byte[] message) {
        super(message);
    }

    public TestWebSocketMessage(MessageHead head) {
        super(head);
    }

    public TestWebSocketMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }

    @Override
    protected MessageHead doBuild(byte[] message) {
        return new DefaultMessageHead(getChannelId(), null, TestProtocolType.WebSocket_Simple);
    }

}
