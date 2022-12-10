package com.dream.iot.websocket;

import com.dream.iot.message.DefaultMessageHead;

public class WebSocketHead extends DefaultMessageHead {

    public WebSocketHead(byte[] message) {
        super(message);
    }

    public WebSocketHead(String equipCode) {
        super(equipCode, null, WebSocketProtocolType.Default_Client);
    }
}
