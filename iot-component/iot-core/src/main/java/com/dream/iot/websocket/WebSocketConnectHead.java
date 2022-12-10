package com.dream.iot.websocket;

public class WebSocketConnectHead extends WebSocketHead {

    public WebSocketConnectHead(byte[] message) {
        super(message);
    }

    public WebSocketConnectHead(String equipCode) {
        super(equipCode);
    }
}
