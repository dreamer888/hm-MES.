package com.dream.iot.test.mqtt;

import com.dream.iot.client.mqtt.gateway.MqttGatewayComponent;
import com.dream.iot.client.mqtt.gateway.MqttGatewayMessage;
import com.dream.iot.client.mqtt.gateway.MqttGatewayProtocol;
import com.dream.iot.client.mqtt.gateway.MqttGatewayProtocolHandle;
import com.dream.iot.client.mqtt.message.MqttMessageHead;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.test.TestConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqttGatewayProtocolTestHandle implements MqttGatewayProtocolHandle {

    @Autowired
    private MqttGatewayComponent component;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object handle(MqttGatewayProtocol protocol) {
        MqttGatewayMessage message = protocol.requestMessage();
        MqttMessageHead head = message.getHead();
        if(protocol.getExecStatus() == ExecStatus.success) {
            logger.info(TestConst.LOGGER_MQTT_PROTOCOL_DESC, component.getName(), message.getQos()
                    , message.getTopic(), head.getEquipCode(), head.getMessageId(), "通过" );
        } else {
            logger.error(TestConst.LOGGER_MQTT_PROTOCOL_DESC, component.getName(), message.getQos()
                    , head.getEquipCode(), head.getMessageId(), protocol.getEquipCode());
        }
        return null;
    }
}
