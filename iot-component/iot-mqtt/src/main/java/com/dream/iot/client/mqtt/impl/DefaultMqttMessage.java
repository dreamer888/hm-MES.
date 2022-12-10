package com.dream.iot.client.mqtt.impl;

import com.dream.iot.client.mqtt.message.MqttClientMessage;
import com.dream.iot.client.mqtt.message.MqttMessageHead;
import io.netty.handler.codec.mqtt.MqttQoS;

public class DefaultMqttMessage extends MqttClientMessage {

    public DefaultMqttMessage(byte[] message) {
        super(message);
    }

    public DefaultMqttMessage(MqttMessageHead head, String topic) {
        super(head, topic);
    }

    public DefaultMqttMessage(MqttMessageHead head, MqttQoS qos, String topic) {
        super(head, qos, topic);
    }

    public DefaultMqttMessage(MqttMessageHead head, MessageBody body, String topic) {
        super(head, body, topic);
    }

    public DefaultMqttMessage(MqttMessageHead head, MessageBody body, MqttQoS qos, String topic) {
        super(head, body, qos, topic);
    }

    @Override
    protected MqttMessageHead doBuild(byte[] payload) {
        return new MqttMessageHead(getChannelId(), null, DefaultMqttProtocolType.Subscribe);
    }

}
