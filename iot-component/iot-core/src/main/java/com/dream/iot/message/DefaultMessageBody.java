package com.dream.iot.message;

import com.dream.iot.Message;

import static com.dream.iot.Message.EMPTY;

public class DefaultMessageBody implements Message.MessageBody {

    private byte[] message;

    /**
     * 使用空报文
     */
    public DefaultMessageBody() {
        this(EMPTY);
    }

    public DefaultMessageBody(byte[] message) {
        this.message = message;
    }

    @Override
    public byte[] getMessage() {
        return this.message;
    }
}
