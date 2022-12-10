package com.dream.iot.client;

import com.dream.iot.config.ConnectProperties;

/**
 * 创建客户端
 */
public interface ClientFactory {

    IotClient createNewClient(ConnectProperties config);
}
