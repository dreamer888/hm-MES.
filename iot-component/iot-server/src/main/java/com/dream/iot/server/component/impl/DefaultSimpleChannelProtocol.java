package com.dream.iot.server.component.impl;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.ProtocolHandle;
import com.dream.iot.business.BusinessFactory;
import com.dream.iot.server.ServerSocketProtocol;

public class DefaultSimpleChannelProtocol extends ServerSocketProtocol<DefaultSimpleServerMessage> {

    @Override
    public AbstractProtocol buildRequestMessage() {
        return null;
    }

    @Override
    public AbstractProtocol buildResponseMessage() {
        return null;
    }

    @Override
    public AbstractProtocol exec(BusinessFactory factory) {
        return null;
    }

    @Override
    public AbstractProtocol exec(ProtocolHandle handle) {
        return null;
    }

    @Override
    public <T> T protocolType() {
        return null;
    }
}
