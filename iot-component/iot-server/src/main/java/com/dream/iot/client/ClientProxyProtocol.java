package com.dream.iot.client;

import com.dream.iot.ProtocolType;
import com.dream.iot.proxy.ProxyServerMessage;
import com.dream.iot.server.protocol.ServerInitiativeProtocol;

import java.io.IOException;

public class ClientProxyProtocol extends ServerInitiativeProtocol<ProxyServerMessage> {

    @Override
    protected ProxyServerMessage doBuildRequestMessage() throws IOException {
        return null;
    }

    @Override
    protected void doBuildResponseMessage(ProxyServerMessage message) {

    }

    @Override
    public ProtocolType protocolType() {
        return null;
    }
}
