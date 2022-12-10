package com.dream.iot.client.mqtt.gateway;

import com.dream.iot.client.mqtt.MqttConnectProperties;
import io.netty.handler.codec.mqtt.MqttQoS;

/**
 * @see #getClientId() 标识唯一的mqtt连接 必填
 */
public class MqttGatewayConnectProperties extends MqttConnectProperties {

    /**
     * 发送的主题
     */
    private String topic;

    /**
     * 发送的主题等级
     */
    private MqttQoS qoS;

    public MqttGatewayConnectProperties(String host, String clientId, String topic) {
        this(host, 1883, clientId, topic, MqttQoS.AT_LEAST_ONCE);
    }

    public MqttGatewayConnectProperties(String host, Integer port, String clientId, String topic) {
        this(host, port, clientId, topic, MqttQoS.AT_LEAST_ONCE);
    }

    public MqttGatewayConnectProperties(String host, Integer port, String clientId, String topic, MqttQoS qoS) {
        super(host, port, clientId);
        this.topic = topic;
        this.qoS = qoS;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public MqttQoS getQoS() {
        return qoS;
    }

    public void setQoS(MqttQoS qoS) {
        this.qoS = qoS;
    }
}
