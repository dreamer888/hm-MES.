package com.dream.iot.client;

import com.dream.iot.Protocol;
import com.dream.iot.business.BusinessFactory;

public class ClientProtocolHandleFactory extends BusinessFactory<ClientProtocolHandle> {

    @Override
    protected Class<? extends Protocol> getProtocolClass(ClientProtocolHandle item) {
        return item.protocolClass();
    }

    @Override
    protected Class<ClientProtocolHandle> getServiceClass() {
        return ClientProtocolHandle.class;
    }
}
