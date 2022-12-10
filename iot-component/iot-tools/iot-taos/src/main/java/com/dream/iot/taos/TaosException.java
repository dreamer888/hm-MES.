package com.dream.iot.taos;

import com.dream.iot.ProtocolException;

public class TaosException extends ProtocolException {

    public TaosException(String message) {
        super(message);
    }

    public TaosException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaosException(Throwable cause) {
        super(cause);
    }

    public TaosException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
