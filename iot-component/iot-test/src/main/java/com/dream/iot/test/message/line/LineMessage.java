package com.dream.iot.test.message.line;

import com.dream.iot.test.message.TestMessage;

/**
 * 基于换行符的测试报文实体
 */
public class LineMessage extends TestMessage {

    public LineMessage(byte[] message) {
        super(message);
    }

    public LineMessage(LineMessageHead head, LineMessageBody body) {
        super(head, body);
    }

    @Override
    protected LineMessageHead doBuild(byte[] message) {
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
