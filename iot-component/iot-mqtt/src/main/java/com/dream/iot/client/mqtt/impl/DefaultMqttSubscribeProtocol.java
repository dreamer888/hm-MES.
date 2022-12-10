package com.dream.iot.client.mqtt.impl;

import com.dream.iot.ProtocolType;
import com.dream.iot.client.protocol.ServerInitiativeProtocol;
import io.netty.handler.codec.mqtt.MqttQoS;

public class DefaultMqttSubscribeProtocol extends ServerInitiativeProtocol<DefaultMqttMessage> {

    private String topic;
    private MqttQoS qoS;

    public DefaultMqttSubscribeProtocol(DefaultMqttMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected void doBuildRequestMessage(DefaultMqttMessage requestMessage) {
        this.qoS = requestMessage.getQos();
        this.topic = requestMessage.getTopic();
    }

    @Override
    protected DefaultMqttMessage doBuildResponseMessage() {
        return null;
    }

    @Override
    public ProtocolType protocolType() {
        return DefaultMqttProtocolType.Subscribe;
    }

    public String getTopic() {
        return topic;
    }

    public MqttQoS getQoS() {
        return qoS;
    }
}
