package com.dream.iot.client.mqtt.impl;

import com.dream.iot.client.mqtt.MqttConnectProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "iot.mqtt.default")
public class DefaultMqttConnectProperties extends MqttConnectProperties {

    private final static String DEFAULT_CLIENT = "IOT:DEFAULT:CLIENT:ID";

    public DefaultMqttConnectProperties() {
        super(DEFAULT_CLIENT);
    }
}
