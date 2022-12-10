package com.dream.iot.websocket;

import com.dream.iot.ProtocolException;

public class WebSocketException extends ProtocolException {

    public WebSocketException(String message) {
        super(message);
    }

    public WebSocketException(String message, Object protocol) {
        super(message, protocol);
    }

    public WebSocketException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebSocketException(Throwable cause, Object protocol) {
        super(cause, protocol);
    }

    public WebSocketException(String message, Throwable cause, Object protocol) {
        super(message, cause, protocol);
    }
}
