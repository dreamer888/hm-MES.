package com.dream.iot.plc;

import com.dream.iot.client.ClientMessage;

public abstract class PlcClientMessage extends ClientMessage {

    public PlcClientMessage(byte[] message) {
        super(message);
    }

    public PlcClientMessage(MessageHead head) {
        super(head);
    }

    public PlcClientMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }
}
