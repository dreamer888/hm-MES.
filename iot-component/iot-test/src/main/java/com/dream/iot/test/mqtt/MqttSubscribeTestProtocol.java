package com.dream.iot.test.mqtt;

import com.dream.iot.Message;
import com.dream.iot.ProtocolType;
import com.dream.iot.client.mqtt.MqttClientException;
import com.dream.iot.client.mqtt.impl.DefaultMqttComponent;
import com.dream.iot.client.protocol.ServerInitiativeProtocol;
import com.dream.iot.test.TestConst;
import com.dream.iot.test.TestProtocolType;
import io.netty.handler.codec.mqtt.MqttQoS;

/**
 * mqtt订阅测试协议
 * @see DefaultMqttComponent 测试默认实现的Mqtt组件
 */
public class MqttSubscribeTestProtocol extends ServerInitiativeProtocol<MqttClientTestMessage> {

    public MqttSubscribeTestProtocol(MqttClientTestMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected void doBuildRequestMessage(MqttClientTestMessage requestMessage) {
        Message.MessageHead head = requestMessage.getHead();

        // 遗嘱主题订阅
        String topic = requestMessage.getTopic();
        switch (topic) {
            case MqttClientTestHandle.EXACTLY_ONCE_TOPIC:
                logger.info(TestConst.LOGGER_PROTOCOL_DESC, "mqtt协议", MqttQoS.EXACTLY_ONCE
                        , head.getEquipCode(), head.getMessageId(), "通过");
                break;
            case MqttClientTestHandle.AT_LEAST_ONCE_TOPIC:
                logger.info(TestConst.LOGGER_PROTOCOL_DESC, "mqtt协议", MqttQoS.AT_LEAST_ONCE
                        , head.getEquipCode(), head.getMessageId(), "通过");
                break;
            case MqttClientTestHandle.AT_MOST_ONCE_TOPIC:
                logger.info(TestConst.LOGGER_PROTOCOL_DESC, "mqtt协议", MqttQoS.AT_MOST_ONCE
                        , head.getEquipCode(), head.getMessageId(), "通过");
                break;
            default:
                throw new MqttClientException("不支持的topic");
        }
    }

    @Override
    protected MqttClientTestMessage doBuildResponseMessage() {

        return null;

    }

    @Override
    public ProtocolType protocolType() {
        return TestProtocolType.PIReq;
    }
}
