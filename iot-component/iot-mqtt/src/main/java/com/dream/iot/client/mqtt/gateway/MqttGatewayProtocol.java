package com.dream.iot.client.mqtt.gateway;

import com.dream.iot.ProtocolType;
import com.dream.iot.client.protocol.ClientInitiativeProtocol;

/**
 * mqtt网关协议
 * @see MqttGatewayProtocolHandle 可以用来处理发布的状态
 */
public class MqttGatewayProtocol extends ClientInitiativeProtocol<MqttGatewayMessage> {

    private MqttGatewayMessage request;

    public MqttGatewayProtocol(MqttGatewayMessage request) {
        this.request = request;
    }

    @Override
    protected MqttGatewayMessage doBuildRequestMessage() {
        return this.request;
    }

    @Override
    public void doBuildResponseMessage(MqttGatewayMessage responseMessage) {

    }

    @Override
    public ProtocolType protocolType() {
        return MqttGatewayProtocolType.Mqtt_Gateway;
    }
}
