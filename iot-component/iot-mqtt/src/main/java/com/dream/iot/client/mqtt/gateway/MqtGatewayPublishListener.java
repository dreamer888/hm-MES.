package com.dream.iot.client.mqtt.gateway;

import com.dream.iot.client.IotClientBootstrap;
import com.dream.iot.client.mqtt.MessageMapper;
import com.dream.iot.client.mqtt.MessagePublishListener;
import com.dream.iot.client.mqtt.MqttClient;
import com.dream.iot.consts.ExecStatus;

public class MqtGatewayPublishListener implements MessagePublishListener {

    @Override
    public void success(MqttClient client, MessageMapper mapper) {
        String messageId = mapper.getMessage().getHead().getMessageId();
        MqttGatewayProtocol protocol = (MqttGatewayProtocol)client.getClientComponent().remove(messageId);
        if(protocol != null) {
            protocol.setExecStatus(ExecStatus.success);
            protocol.exec(IotClientBootstrap.businessFactory);
        }
    }
}
