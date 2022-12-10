package com.dream.iot.client;

import com.dream.iot.ProtocolHandle;
import org.springframework.core.GenericTypeResolver;

public interface ClientProtocolHandle<T extends ClientProtocol> extends ProtocolHandle<T> {

    default Class<T> protocolClass() {
        return (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), ClientProtocolHandle.class);
    }

}
