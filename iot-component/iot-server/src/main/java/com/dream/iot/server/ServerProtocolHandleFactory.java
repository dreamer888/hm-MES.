package com.dream.iot.server;

import com.dream.iot.Protocol;
import com.dream.iot.business.BusinessFactory;

public class ServerProtocolHandleFactory extends BusinessFactory<ServerProtocolHandle> {

    @Override
    protected Class<? extends Protocol> getProtocolClass(ServerProtocolHandle item) {
        return item.protocolClass();
    }

    @Override
    protected Class<ServerProtocolHandle> getServiceClass() {
        return ServerProtocolHandle.class;
    }
}
