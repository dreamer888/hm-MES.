package com.dream.iot.websocket;

import com.dream.iot.message.DefaultMessageHead;

public class WebSocketCloseHead extends DefaultMessageHead {

    public WebSocketCloseHead(String equipCode) {
        super(equipCode, null, WebSocketProtocolType.Close);
    }
}
