package com.dream.iot.test.mqtt;

import cn.hutool.json.JSONUtil;
import com.dream.iot.client.mqtt.message.MqttMessageHead;
import com.dream.iot.client.protocol.ClientInitiativeProtocol;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.test.*;
import io.netty.handler.codec.mqtt.MqttQoS;

import java.nio.charset.StandardCharsets;

/**
 * create time: 2021/9/3
 *  客户端主动请求Mqtt Broker请求协议
 * @author dream
 * @since 1.0
 */
public class MqttPublishTestProtocol extends ClientInitiativeProtocol<MqttClientTestMessage> {

    private MqttQoS qoS;
    private String topic;
    private String deviceSn;

    public MqttPublishTestProtocol(String topic) {
        this(MqttQoS.AT_MOST_ONCE, topic, "MqttTestSn");
    }

    public MqttPublishTestProtocol(MqttQoS qoS, String topic, String deviceSn) {
        this.qoS = qoS;
        this.topic = topic;
        this.deviceSn = deviceSn;
    }

    @Override
    protected MqttClientTestMessage doBuildRequestMessage() {
        MqttMessageHead messageHead = new MqttMessageHead(this.deviceSn, ClientSnGen.getMessageId(), protocolType());
        messageHead.setMessage(JSONUtil.toJsonStr(messageHead).getBytes(StandardCharsets.UTF_8));
        return new MqttClientTestMessage(messageHead, this.qoS, this.topic);
    }

    @Override
    public void doBuildResponseMessage(MqttClientTestMessage responseMessage) {
        MqttClientTestMessage message = requestMessage();
        MqttMessageHead head = message.getHead();
        if(getExecStatus() == ExecStatus.success) {
            logger.info(TestConst.LOGGER_MQTT_PROTOCOL_DESC, "mqtt(Publish)", this.qoS
                    , this.topic, head.getEquipCode(), head.getMessageId(), "通过");
        } else {
            logger.error(TestConst.LOGGER_MQTT_PROTOCOL_DESC, "mqtt(Publish)", this.qoS
                    , this.topic, head.getEquipCode(), head.getMessageId(), "失败("+getExecStatus()+")");
        }
    }

    @Override
    public TestProtocolType protocolType() {
        return TestProtocolType.CIReq;
    }

    public String getTopic() {
        return topic;
    }

}
