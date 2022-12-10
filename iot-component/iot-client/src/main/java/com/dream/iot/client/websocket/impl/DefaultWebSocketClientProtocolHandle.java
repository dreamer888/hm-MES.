package com.dream.iot.client.websocket.impl;

import com.dream.iot.Message;
import com.dream.iot.client.ClientProtocolHandle;
import com.dream.iot.client.websocket.WebSocketClientListener;
import com.dream.iot.websocket.WebSocketConnectHead;
import com.dream.iot.websocket.WebSocketException;
import com.dream.iot.websocket.WebSocketFrameType;

public class DefaultWebSocketClientProtocolHandle implements ClientProtocolHandle<DefaultWebSocketClientProtocol> {

    private DefaultWebSocketClientComponent component;

    public DefaultWebSocketClientProtocolHandle(DefaultWebSocketClientComponent component) {
        this.component = component;
    }

    @Override
    public Object handle(DefaultWebSocketClientProtocol protocol) {

        DefaultWebSocketClientMessage requestMessage = protocol.requestMessage();
        WebSocketClientListener listener = requestMessage.getProperties().getListener();
        if(listener != null) {
            Message.MessageHead head = requestMessage.getHead();
            if(head instanceof WebSocketConnectHead) {
                listener.onConnect(protocol);
            } else {
                WebSocketFrameType type = requestMessage.frameType();
                switch (type) {
                    case Text: listener.onText(protocol); break;
                    case Close: listener.onClose(protocol); break;
                    case Binary: listener.onBinary(protocol); break;
                    default: throw new WebSocketException("不支持的事件["+type+"]");
                }
            }
        }

        return null;
    }
}
