package com.dream.iot.client.mqtt.impl;

import com.dream.iot.ProtocolType;

public enum DefaultMqttProtocolType implements ProtocolType {

    Subscribe("订阅"),
    Publish("发布")
    ;

    private String desc;

    DefaultMqttProtocolType(String desc) {
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
