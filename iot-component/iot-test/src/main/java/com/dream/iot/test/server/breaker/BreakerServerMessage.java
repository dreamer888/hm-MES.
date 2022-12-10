package com.dream.iot.test.server.breaker;

import com.dream.iot.server.ServerMessage;
import com.dream.iot.test.MessageCreator;

public class BreakerServerMessage extends ServerMessage {

    public BreakerServerMessage(byte[] message) {
        super(message);
    }

    public BreakerServerMessage(MessageHead head) {
        super(head);
    }

    public BreakerServerMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }

    @Override
    protected MessageHead doBuild(byte[] message) {
        this.messageBody = MessageCreator.buildBreakerBody(message);
        return MessageCreator.buildBreakerHeader(message);
    }
}
