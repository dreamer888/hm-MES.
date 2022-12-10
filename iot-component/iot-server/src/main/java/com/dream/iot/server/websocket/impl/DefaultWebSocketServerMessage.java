package com.dream.iot.server.websocket.impl;

import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.server.websocket.WebSocketServerMessageAbstract;
import com.dream.iot.websocket.WebSocketCloseHead;
import com.dream.iot.websocket.WebSocketFrameType;
import com.dream.iot.websocket.WebSocketProtocolType;

public class DefaultWebSocketServerMessage extends WebSocketServerMessageAbstract {

    public DefaultWebSocketServerMessage(byte[] message) {
        super(message);
    }

    public DefaultWebSocketServerMessage(MessageHead head) {
        super(head);
    }

    public DefaultWebSocketServerMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }

    public DefaultWebSocketServerMessage(MessageHead head, WebSocketFrameType frameType) {
        super(head);
        this.setFrameType(frameType);
    }

    public DefaultWebSocketServerMessage(MessageHead head, MessageBody body, WebSocketFrameType frameType) {
        super(head, body);
        this.setFrameType(frameType);
    }

    @Override
    protected MessageHead doBuild(byte[] message) {
        if(frameType() == WebSocketFrameType.Close) {
            return new WebSocketCloseHead(getChannelId());
        }

        return new DefaultMessageHead(getChannelId(), null, WebSocketProtocolType.Default_Server);
    }

    @Override
    public DefaultWebSocketServerMessage setFrameType(WebSocketFrameType frameType) {
        return (DefaultWebSocketServerMessage) super.setFrameType(frameType);
    }
}
