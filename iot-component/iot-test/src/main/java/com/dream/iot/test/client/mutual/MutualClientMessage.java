package com.dream.iot.test.client.mutual;

import com.dream.iot.test.client.ClientTestMessage;

public class MutualClientMessage extends ClientTestMessage {

    public MutualClientMessage(byte[] message) {
        super(message);
    }

    public MutualClientMessage(MessageHead head) {
        super(head);
    }

    public MutualClientMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }

    @Override
    protected MessageHead doBuild(byte[] message) {
        return null;
    }
}
