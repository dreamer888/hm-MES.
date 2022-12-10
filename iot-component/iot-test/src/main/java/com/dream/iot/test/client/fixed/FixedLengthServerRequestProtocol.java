package com.dream.iot.test.client.fixed;

import cn.hutool.core.util.RandomUtil;
import com.dream.iot.Message;
import com.dream.iot.client.protocol.ServerInitiativeProtocol;
import com.dream.iot.test.MessageCreator;
import com.dream.iot.test.TestProtocolType;

import java.util.concurrent.TimeUnit;

public class FixedLengthServerRequestProtocol extends ServerInitiativeProtocol<FixedLengthClientMessage> {

    public FixedLengthServerRequestProtocol(FixedLengthClientMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected void doBuildRequestMessage(FixedLengthClientMessage requestMessage) {
        int timeout = RandomUtil.randomInt(1, 3);
        try {
            // 用于测试服务端超时
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected FixedLengthClientMessage doBuildResponseMessage() {
        Message.MessageHead head = requestMessage().getHead();
        return MessageCreator.buildFixedLengthClientMessage(Long.valueOf(head.getEquipCode()), head.getMessageId(), protocolType());
    }

    @Override
    public TestProtocolType protocolType() {
        return TestProtocolType.PIReq;
    }
}
