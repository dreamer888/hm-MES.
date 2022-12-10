package com.dream.iot.client.websocket.impl;

import com.dream.iot.client.websocket.WebSocketClientMessage;
import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.websocket.WebSocketCloseHead;
import com.dream.iot.websocket.WebSocketFrameType;
import com.dream.iot.websocket.WebSocketProtocolType;

public class DefaultWebSocketClientMessage extends WebSocketClientMessage {

    private WebSocketFrameType frameType;

    public DefaultWebSocketClientMessage(byte[] message) {
        super(message);
    }

    public DefaultWebSocketClientMessage(MessageHead head) {
        super(head);
    }

    public DefaultWebSocketClientMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }

    @Override
    protected MessageHead doBuild(byte[] message) {
        if(frameType() == WebSocketFrameType.Close) {
            return new WebSocketCloseHead(getChannelId());
        }

        return new DefaultMessageHead(getChannelId(), null, WebSocketProtocolType.Default_Client);
    }

    @Override
    public WebSocketFrameType frameType() {
        return this.frameType;
    }

    @Override
    public DefaultWebSocketClientMessage setFrameType(WebSocketFrameType frameType) {
        this.frameType = frameType;
        return this;
    }
}
