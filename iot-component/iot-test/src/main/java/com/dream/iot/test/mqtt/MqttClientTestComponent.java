package com.dream.iot.test.mqtt;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.SocketMessage;
import com.dream.iot.client.mqtt.MqttClientComponent;
import com.dream.iot.client.mqtt.MqttConnectProperties;
import com.dream.iot.client.mqtt.message.MqttMessageHead;
import com.dream.iot.test.IotTestProperties;
import com.dream.iot.test.TestMultiClientManager;
import com.dream.iot.test.TestProtocolType;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttTopicSubscription;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MqttClientTestComponent extends MqttClientComponent<MqttClientTestMessage> {

    @Autowired
    private IotTestProperties properties;


    public MqttClientTestComponent() { }

    public MqttClientTestComponent(MqttConnectProperties config) {
        super(config, new TestMultiClientManager());
    }

    @Override
    protected List<MqttTopicSubscription> doSubscribe(MqttConnectProperties client) {
        List<MqttTopicSubscription> subscriptionList = new ArrayList<>();
        if(!client.isWillRetain()) {
            IotTestProperties.TestMqttConnectProperties properties = (IotTestProperties.TestMqttConnectProperties) client;
            subscriptionList.add(new MqttTopicSubscription(MqttClientTestHandle.TOPIC_RESPONSE+"/"+properties.getDeviceSn(), MqttQoS.AT_MOST_ONCE));
        }

        return subscriptionList;
    }

    @Override
    public String getName() {
        return "mqtt自定义";
    }

    @Override
    public String getDesc() {
        return "用于测试mqtt协议";
    }

    @Override
    public Class<MqttClientTestMessage> getMessageClass() {
        return MqttClientTestMessage.class;
    }

    @Override
    public SocketMessage createMessage(byte[] message) {
        return new MqttClientTestMessage(message);
    }

    @Override
    public AbstractProtocol getProtocol(MqttClientTestMessage message) {
        MqttMessageHead head = message.getHead();
        if(head.getType() == TestProtocolType.CIReq) {
            return remove(head.getMessageId());
        } else {
            return null;
        }
    }
}
