package com.dream.iot.client.mqtt.gateway;

import com.dream.iot.ProtocolType;

public enum MqttGatewayProtocolType implements ProtocolType {
    Mqtt_Gateway("Mqtt Gateway Default Impl");

    private String desc;

    MqttGatewayProtocolType(String desc) {
        this.desc = desc;
    }

    @Override
    public Enum getType() {
        return this;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
