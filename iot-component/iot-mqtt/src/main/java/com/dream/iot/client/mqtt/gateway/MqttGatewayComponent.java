package com.dream.iot.client.mqtt.gateway;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.MultiClientManager;
import com.dream.iot.client.mqtt.MessagePublishListener;
import com.dream.iot.client.mqtt.MqttClientComponent;
import com.dream.iot.client.mqtt.MqttConnectProperties;
import io.netty.handler.codec.mqtt.MqttTopicSubscription;

import java.util.List;

/**
 *
 */
public class MqttGatewayComponent extends MqttClientComponent<MqttGatewayMessage> {

    public MqttGatewayComponent() { }

    public MqttGatewayComponent(MqttGatewayConnectProperties config) {
        super(config);
    }

    public MqttGatewayComponent(MqttGatewayConnectProperties config, MultiClientManager clientManager) {
        super(config, clientManager);
    }

    public MqttGatewayComponent(ClientConnectProperties config
            , MultiClientManager clientManager, MessagePublishListener publishListener) {
        super(config, clientManager, publishListener);
    }

    @Override
    public String getName() {
        return "MQTT(Gateway)";
    }

    @Override
    public String getDesc() {
        return "用于将其他设备采集的数据简便的发布到mqtt服务器";
    }

    @Override
    public Class<MqttGatewayMessage> getMessageClass() {
        return MqttGatewayMessage.class;
    }

    @Override
    public MqttGatewayMessage createMessage(byte[] message) {
        return new MqttGatewayMessage(message);
    }

    @Override
    public AbstractProtocol getProtocol(MqttGatewayMessage message) {
        return null;
    }

    @Override
    protected List<MqttTopicSubscription> doSubscribe(MqttConnectProperties client) {
        return null;
    }
}
