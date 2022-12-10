package com.dream.iot.test.server.fixed;

import com.dream.iot.server.protocol.ServerInitiativeProtocol;
import com.dream.iot.test.ClientSnGen;
import com.dream.iot.test.MessageCreator;
import com.dream.iot.test.TestProtocolType;

import java.io.IOException;

/**
 * create time: 2022/1/16
 *
 * @author dream
 * @since 1.0
 */
public class FixedLengthServerRequestProtocol extends ServerInitiativeProtocol<FixedLengthServerMessage> {

    private String deviceSn;

    public FixedLengthServerRequestProtocol(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    @Override
    protected FixedLengthServerMessage doBuildRequestMessage() throws IOException {
        final String messageId = ClientSnGen.getMessageId();
        return MessageCreator.buildFixedLengthServerMessage(this.deviceSn, messageId, 0, protocolType());
    }

    @Override
    protected void doBuildResponseMessage(FixedLengthServerMessage message) {

    }

    @Override
    public TestProtocolType protocolType() {
        return TestProtocolType.PIReq;
    }
}
