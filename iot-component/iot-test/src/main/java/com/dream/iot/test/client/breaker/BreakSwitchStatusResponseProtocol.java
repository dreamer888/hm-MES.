package com.dream.iot.test.client.breaker;

import com.dream.iot.ProtocolType;
import com.dream.iot.client.protocol.ServerInitiativeProtocol;
import com.dream.iot.test.BreakerProtocolType;

public class BreakSwitchStatusResponseProtocol extends ServerInitiativeProtocol<BreakerClientMessage> {

    public BreakSwitchStatusResponseProtocol(BreakerClientMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected void doBuildRequestMessage(BreakerClientMessage requestMessage) {

    }

    @Override
    protected BreakerClientMessage doBuildResponseMessage() {
        return null;
    }

    @Override
    public ProtocolType protocolType() {
        return BreakerProtocolType.SwitchStatus;
    }
}
