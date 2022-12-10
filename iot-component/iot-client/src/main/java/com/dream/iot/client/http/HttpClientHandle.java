package com.dream.iot.client.http;

import com.dream.iot.client.ClientProtocolHandle;

/**
 * 基于http实现的协议的处理器
 * @param <T>
 */
public interface HttpClientHandle<T extends ClientHttpProtocol> extends ClientProtocolHandle<T> {

}
