package com.dream.iot.client.mqtt.impl;

import io.netty.handler.codec.mqtt.MqttTopicSubscription;

/**
 * mqtt订阅监听器
 * @see DefaultMqttComponent Spring容器里面包含监听器对象则默认启用此组件
 */
public interface MqttSubscribeListener {

    /**
     * 要订阅的topic
     * @see DefaultMqttConnectProperties 默认的mqtt broker配置
     * @return
     */
    MqttTopicSubscription topic();

    /**
     * 订阅监听
     */
    void onSubscribe(DefaultMqttSubscribeProtocol protocol);
}
