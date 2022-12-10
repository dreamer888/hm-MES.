package com.dream.iot.test.client.fixed;

import com.dream.iot.client.ClientMessage;
import com.dream.iot.test.MessageCreator;
import com.dream.iot.test.TestProtocolType;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 固定长度报文 28
 *    8         8        4       8
 * 设备编号 + messageId + type + 递增值
 */
public class FixedLengthClientMessage extends ClientMessage {

    private static AtomicLong inc = new AtomicLong(1);

    public FixedLengthClientMessage(byte[] message) {
        super(message);
    }

    public FixedLengthClientMessage(MessageHead head) {
        super(head);
    }

    public FixedLengthClientMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }

    @Override
    protected MessageHead doBuild(byte[] message) {
        return MessageCreator.buildFixedMessageHead(message);
    }

    public static FixedLengthClientMessage buildHeart(long deviceSn) {
        return MessageCreator.buildFixedLengthClientMessage(deviceSn, 0l, TestProtocolType.Heart);
    }

    public static FixedLengthClientMessage build(long deviceSn, TestProtocolType protocolType) {
        return MessageCreator.buildFixedLengthClientMessage(deviceSn, inc.getAndIncrement(), protocolType);
    }
}
