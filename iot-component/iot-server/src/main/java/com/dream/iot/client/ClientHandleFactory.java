package com.dream.iot.client;

import com.dream.iot.client.handle.MethodMeta;
import com.dream.iot.proxy.ProxyServerMessage;

/**
 * create time: 2021/3/4
 *
 * @author dream
 * @since 1.0
 */
public interface ClientHandleFactory {

    MethodMeta getHandle(String tradeType);

    ParamResolver getResolver(Class<? extends ParamResolver> resolver);

    <T> T getRelation(ProxyServerMessage message);
}
