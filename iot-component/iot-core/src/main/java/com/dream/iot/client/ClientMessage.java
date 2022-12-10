package com.dream.iot.client;

import com.dream.iot.message.UnParseBodyMessage;

public abstract class ClientMessage extends UnParseBodyMessage {

    public ClientMessage(byte[] message) {
        super(message);
    }

    public ClientMessage(MessageHead head) {
        super(head);
    }

    public ClientMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }

    /**
     * 解析出message到指定的报文头对象
     * @see #message {@link #messageHead} 和 {@link #messageBody}
     * @param message
     */
    @Override
    protected abstract MessageHead doBuild(byte[] message);

    public String getMessageId() {
        MessageHead head = getHead();
        return head != null ? head.getMessageId() : null;
    }

}
