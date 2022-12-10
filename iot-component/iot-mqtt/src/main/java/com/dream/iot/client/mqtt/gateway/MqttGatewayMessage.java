package com.dream.iot.client.mqtt.gateway;

import com.dream.iot.client.mqtt.message.MqttClientMessage;
import com.dream.iot.client.mqtt.message.MqttMessageHead;
import io.netty.handler.codec.mqtt.MqttQoS;

public class MqttGatewayMessage extends MqttClientMessage {

    public MqttGatewayMessage(byte[] message) {
        super(message);
    }

    public MqttGatewayMessage(MqttMessageHead head, String topic) {
        super(head, topic);
    }

    public MqttGatewayMessage(MqttMessageHead head, MqttQoS qos, String topic) {
        super(head, qos, topic);
    }

    public MqttGatewayMessage(MqttMessageHead head, MessageBody body, String topic) {
        super(head, body, topic);
    }

    public MqttGatewayMessage(MqttMessageHead head, MessageBody body, MqttQoS qos, String topic) {
        super(head, body, qos, topic);
    }

    @Override
    protected MqttMessageHead doBuild(byte[] payload) {
        return null;
    }
}
