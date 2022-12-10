package com.dream.iot.client;

import com.dream.iot.Protocol;
import com.dream.iot.ProtocolException;

/**
 * 协议不可写异常
 */
public class UnWritableProtocolException extends ProtocolException {

    /**
     * @see com.dream.iot.Protocol
     * @param protocol
     */
    public UnWritableProtocolException(Object protocol) {
        super(protocol);
    }

    public UnWritableProtocolException(String message, Object protocol) {
        super(message, protocol);
    }

    @Override
    public Protocol getProtocol() {
        return (Protocol) super.getProtocol();
    }
}
