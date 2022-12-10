package com.dream.iot.client;

import com.dream.iot.proxy.ProxyClientMessage;
import com.dream.iot.proxy.ProxyServerMessage;

/**
 * create time: 2021/3/4
 *  参数解析器, 非线程安全
 * @author dream
 * @since 1.0
 */
public interface ParamResolver {

    String getDeviceSn(ProxyClientMessage message);

    String getTradeType(ProxyClientMessage message);

    Object resolver(String name, Class type, ProxyServerMessage message);
}
