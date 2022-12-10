package com.dream.iot.server.udp.impl;

import com.dream.iot.server.ServerProtocolHandle;

public interface DefaultUdpServerProtocolHandle extends ServerProtocolHandle<DefaultUdpServerProtocol> {

    @Override
    Object handle(DefaultUdpServerProtocol protocol);

    @Override
    default Class<DefaultUdpServerProtocol> protocolClass() {
        return DefaultUdpServerProtocol.class;
    }
}
