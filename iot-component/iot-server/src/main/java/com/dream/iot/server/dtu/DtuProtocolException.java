package com.dream.iot.server.dtu;

import com.dream.iot.ProtocolException;

public class DtuProtocolException extends ProtocolException {

    public DtuProtocolException(Object protocol) {
        super(protocol);
    }

    public DtuProtocolException(String message) {
        super(message);
    }

    public DtuProtocolException(String message, Object protocol) {
        super(message, protocol);
    }

    public DtuProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public DtuProtocolException(Throwable cause) {
        super(cause);
    }

    public DtuProtocolException(Throwable cause, Object protocol) {
        super(cause, protocol);
    }

    public DtuProtocolException(String message, Throwable cause, Object protocol) {
        super(message, cause, protocol);
    }

    public DtuProtocolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
