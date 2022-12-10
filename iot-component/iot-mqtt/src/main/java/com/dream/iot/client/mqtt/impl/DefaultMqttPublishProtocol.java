package com.dream.iot.client.mqtt.impl;

import com.dream.iot.ProtocolType;
import com.dream.iot.client.mqtt.message.MqttMessageHead;
import com.dream.iot.client.protocol.ClientInitiativeProtocol;
import io.netty.handler.codec.mqtt.MqttQoS;

public class DefaultMqttPublishProtocol extends ClientInitiativeProtocol<DefaultMqttMessage> {

    private byte[] msg;

    private MqttQoS qoS;

    private String topic;

    public DefaultMqttPublishProtocol(byte[] msg, String topic) {
        this(msg, MqttQoS.AT_MOST_ONCE, topic);
    }

    public DefaultMqttPublishProtocol(byte[] msg, MqttQoS qoS, String topic) {
        this.msg = msg;
        this.qoS = qoS;
        this.topic = topic;
    }

    @Override
    protected DefaultMqttMessage doBuildRequestMessage() {
        MqttMessageHead messageHead = new MqttMessageHead(this.msg);
        return new DefaultMqttMessage(messageHead, this.qoS, this.topic);
    }

    @Override
    public void doBuildResponseMessage(DefaultMqttMessage responseMessage) {

    }

    @Override
    public ProtocolType protocolType() {
        return DefaultMqttProtocolType.Publish;
    }
}
