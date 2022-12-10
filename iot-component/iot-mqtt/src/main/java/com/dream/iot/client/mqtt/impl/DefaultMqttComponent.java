package com.dream.iot.client.mqtt.impl;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.IotClientBootstrap;
import com.dream.iot.client.MultiClientManager;
import com.dream.iot.client.mqtt.MessagePublishListener;
import com.dream.iot.client.mqtt.MqttClientComponent;
import com.dream.iot.client.mqtt.MqttClientException;
import com.dream.iot.client.mqtt.MqttConnectProperties;
import io.netty.handler.codec.mqtt.MqttTopicSubscription;

import java.util.ArrayList;
import java.util.List;

/**
 * Mqtt客户端默认实现
 * @see MqttSubscribeListener spring容器存在此对象将启用此组件
 */
public class DefaultMqttComponent extends MqttClientComponent<DefaultMqttMessage> {

    private static final String NAME = "MQTT<默认>";
    private static final String DESC = "MQTT默认客户端实现";

    public DefaultMqttComponent() { }

    public DefaultMqttComponent(MqttConnectProperties config) {
        super(config);
    }

    public DefaultMqttComponent(MqttConnectProperties config, MultiClientManager clientManager) {
        super(config, clientManager);
    }

    public DefaultMqttComponent(ClientConnectProperties config, MultiClientManager clientManager, MessagePublishListener publishListener) {
        super(config, clientManager, publishListener);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDesc() {
        return DESC;
    }

    @Override
    public AbstractProtocol getProtocol(DefaultMqttMessage message) {
        // 使用监听的方式处理
        return new DefaultMqttSubscribeProtocol(message);
    }

    @Override
    protected List<MqttTopicSubscription> doSubscribe(MqttConnectProperties client) {
        List<MqttTopicSubscription> subscriptions = new ArrayList<>();
        IotClientBootstrap.applicationContext.getAutowireCapableBeanFactory()
                .getBeanProvider(MqttSubscribeListenerManager.class).ifAvailable((manager) -> {
            manager.getListeners().forEach(item -> {
                MqttTopicSubscription topic = item.topic();
                if(topic == null) {
                    throw new MqttClientException("mqtt监听器["+item.getClass().getSimpleName()+"]没有返回topic[null]");
                }

                subscriptions.add(topic);
            });
        });

        return subscriptions;
    }

    @Override
    public Class<DefaultMqttMessage> getMessageClass() {
        return DefaultMqttMessage.class;
    }

    @Override
    public DefaultMqttMessage createMessage(byte[] message) {
        return new DefaultMqttMessage(message);
    }
}
