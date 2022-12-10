package com.dream.iot.plc;

import com.dream.iot.client.ClientProtocolException;

public class PlcException extends ClientProtocolException {

    public PlcException(Object protocol) {
        super(protocol);
    }

    public PlcException(String message) {
        super(message);
    }

    public PlcException(String message, Object protocol) {
        super(message, protocol);
    }

    public PlcException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlcException(Throwable cause) {
        super(cause);
    }

    public PlcException(String message, Throwable cause, Object protocol) {
        super(message, cause, protocol);
    }

    public PlcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
