package com.dream.iot.test.server.fixed;

import com.dream.iot.server.ServerMessage;
import com.dream.iot.test.MessageCreator;

/**
 * 固定长度报文 28
 *    8         8        4       8
 * 设备编号 + messageId + type + 递增值
 */
public class FixedLengthServerMessage extends ServerMessage {

    public FixedLengthServerMessage(byte[] message) {
        super(message);
    }

    public FixedLengthServerMessage(MessageHead head) {
        super(head);
    }

    public FixedLengthServerMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }

    @Override
    protected MessageHead doBuild(byte[] message) {
        return MessageCreator.buildFixedMessageHead(message);
    }
}
