package com.dream.iot.client;

import com.dream.iot.ProtocolException;

public class ClientProtocolException extends ProtocolException {

    public ClientProtocolException(Object protocol) {
        super(protocol);
    }

    public ClientProtocolException(String message) {
        super(message);
    }

    public ClientProtocolException(String message, Object protocol) {
        super(message, protocol);
    }

    public ClientProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientProtocolException(Throwable cause, Object protocol) {
        super(cause, protocol);
    }

    public ClientProtocolException(String message, Throwable cause, Object protocol) {
        super(message, cause, protocol);
    }

    public ClientProtocolException(Throwable cause) {
        super(cause);
    }

    public ClientProtocolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
