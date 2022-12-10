package com.dream.iot.client.mqtt.gateway;

import com.dream.iot.ProtocolType;
import com.dream.iot.client.mqtt.message.MqttMessageHead;
import com.dream.iot.utils.UniqueIdGen;

public class MqttGatewayHead extends MqttMessageHead {

    private static final String prefix = "MG:";

    public MqttGatewayHead(byte[] message) {
        super(message);
    }

    public MqttGatewayHead(String equipCode) {
        this(equipCode, UniqueIdGen.messageId(prefix), MqttGatewayProtocolType.Mqtt_Gateway);
    }

    public MqttGatewayHead(String equipCode, String messageId, ProtocolType type) {
        super(equipCode, messageId, type);
    }
}
