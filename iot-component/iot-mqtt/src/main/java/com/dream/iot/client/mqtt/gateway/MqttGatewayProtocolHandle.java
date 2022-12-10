package com.dream.iot.client.mqtt.gateway;

import com.dream.iot.client.ClientProtocolHandle;

public interface MqttGatewayProtocolHandle extends ClientProtocolHandle<MqttGatewayProtocol> {

    @Override
    Object handle(MqttGatewayProtocol protocol);
}
