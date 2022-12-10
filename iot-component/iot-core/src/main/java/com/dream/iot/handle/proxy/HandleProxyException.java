package com.dream.iot.handle.proxy;

public class HandleProxyException extends RuntimeException{

    public HandleProxyException() {
        super();
    }

    public HandleProxyException(String message) {
        super(message);
    }

    public HandleProxyException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandleProxyException(Throwable cause) {
        super(cause);
    }

    protected HandleProxyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
