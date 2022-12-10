package com.dream.iot.server.component.impl;

import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.server.ServerMessage;

public class DefaultSimpleServerMessage extends ServerMessage {

    public DefaultSimpleServerMessage(byte[] message) {
        super(message);
    }

    public DefaultSimpleServerMessage(MessageHead head) {
        super(head);
    }

    public DefaultSimpleServerMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }

    @Override
    protected MessageHead doBuild(byte[] message) {
        return new DefaultMessageHead(getChannelId(), getChannelId(), null);
    }
}
