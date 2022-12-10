package com.dream.iot.test.client.breaker;

import com.dream.iot.client.ClientMessage;
import com.dream.iot.test.MessageCreator;

/**
 * 测试数据采集如：redis和Taos数据库适配功能
 */
public class BreakerClientMessage extends ClientMessage {

    public BreakerClientMessage(byte[] message) {
        super(message);
    }

    public BreakerClientMessage(MessageHead head) {
        super(head);
    }

    public BreakerClientMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }

    @Override
    protected MessageHead doBuild(byte[] message) {
        return MessageCreator.buildBreakerHeader(message);
    }
}
