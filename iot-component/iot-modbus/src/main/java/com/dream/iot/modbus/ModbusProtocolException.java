package com.dream.iot.modbus;

import com.dream.iot.ProtocolException;

public class ModbusProtocolException extends ProtocolException {

    public ModbusProtocolException(Object protocol) {
        super(protocol);
    }

    public ModbusProtocolException(String message) {
        super(message);
    }

    public ModbusProtocolException(String message, Object protocol) {
        super(message, protocol);
    }

    public ModbusProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModbusProtocolException(Throwable cause) {
        super(cause);
    }

    public ModbusProtocolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
