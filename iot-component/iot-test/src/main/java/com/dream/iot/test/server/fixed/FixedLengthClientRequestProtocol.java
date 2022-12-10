package com.dream.iot.test.server.fixed;

import cn.hutool.core.util.RandomUtil;
import com.dream.iot.DeviceManager;
import com.dream.iot.Message;
import com.dream.iot.ProtocolType;
import com.dream.iot.server.manager.DevicePipelineManager;
import com.dream.iot.server.protocol.ClientInitiativeProtocol;
import com.dream.iot.test.MessageCreator;
import com.dream.iot.test.TestProtocolType;

import java.util.concurrent.TimeUnit;

public class FixedLengthClientRequestProtocol extends ClientInitiativeProtocol<FixedLengthServerMessage> {

    public FixedLengthClientRequestProtocol(FixedLengthServerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected FixedLengthServerMessage doBuildResponseMessage() {
        Message.MessageHead head = requestMessage().getHead();
        String equipCode = head.getEquipCode();
        String messageId = head.getMessageId();
        DeviceManager instance = DevicePipelineManager.getInstance(FixedLengthServerMessage.class);
        return MessageCreator.buildFixedLengthServerMessage(equipCode, messageId, instance.useSize(), head.getType());
    }

    @Override
    protected void doBuildRequestMessage(FixedLengthServerMessage requestMessage) {
        int timeout = RandomUtil.randomInt(1, 3);
        try {
            // 用于测试服务端超时
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProtocolType protocolType() {
        return TestProtocolType.CIReq;
    }
}
