package com.dream.iot.client.mqtt.gateway.adapter;

import com.dream.iot.Protocol;
import com.dream.iot.client.mqtt.gateway.MqttGatewayHandle;

/**
 * 直接发布二进制数据
 * @param <T>
 */
public interface MqttGatewayByteHandle<T extends Protocol> extends MqttGatewayHandle<T, byte[]> {

    @Override
    byte[] handle(T protocol);
}
