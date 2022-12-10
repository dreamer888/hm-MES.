package com.dream.iot.test.message;

import com.dream.iot.server.ServerMessage;

public abstract class TestMessage extends ServerMessage {

    public TestMessage(byte[] message) {
        super(message);
    }

    public TestMessage(TMessageHead head) {
        super(head);
    }

    public TestMessage(TMessageHead head, TMessageBody body) {
        super(head, body);
    }

    @Override
    public TMessageHead getHead() {
        return (TMessageHead) super.getHead();
    }

    @Override
    public TMessageBody getBody() {
        return (TMessageBody) super.getBody();
    }
}
