package com.dream.iot.client.mqtt.gateway.adapter;

import com.dream.iot.Protocol;
import com.dream.iot.client.mqtt.gateway.MqttGatewayHandle;

/**
 * 发布json格式的数据
 * @param <T>
 */
public interface MqttGatewayJsonHandle<T extends Protocol, E> extends MqttGatewayHandle<T, E> {

    /**
     * @param protocol
     * @return 返回的对象将格式化成json格式然后进行发布
     */
    @Override
    Object handle(T protocol);
}
