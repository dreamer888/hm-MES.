package com.dream.iot.test.client;

import com.dream.iot.client.ClientMessage;

public abstract class ClientTestMessage extends ClientMessage {

    public ClientTestMessage(byte[] message) {
        super(message);
    }

    public ClientTestMessage(MessageHead head) {
        super(head);
    }

    public ClientTestMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }
}
