package com.dream.iot.test.client.line;

import com.dream.iot.client.ClientMessage;
import com.dream.iot.test.message.line.LineMessageBody;
import com.dream.iot.test.message.line.LineMessageHead;

public class LineClientMessage extends ClientMessage {

    public LineClientMessage(byte[] message) {
        super(message);
    }

    public LineClientMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }

    @Override
    protected MessageHead doBuild(byte[] message) {
        return LineMessageHead.buildHeader(message);
    }

    @Override
    public LineMessageHead getHead() {
        return (LineMessageHead) super.getHead();
    }

    @Override
    public LineMessageBody getBody() {
        return (LineMessageBody) super.getBody();
    }
}
