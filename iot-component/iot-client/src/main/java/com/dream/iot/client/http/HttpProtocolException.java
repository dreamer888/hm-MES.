package com.dream.iot.client.http;

import com.dream.iot.ProtocolException;

public class HttpProtocolException extends ProtocolException {

    public HttpProtocolException(Object protocol) {
        super(protocol);
    }

    public HttpProtocolException(String message) {
        super(message);
    }

    public HttpProtocolException(String message, Object protocol) {
        super(message, protocol);
    }

    public HttpProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpProtocolException(Throwable cause) {
        super(cause);
    }

    public HttpProtocolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
