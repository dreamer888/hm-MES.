package com.dream.iot.test.mqtt;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dream.iot.client.mqtt.message.MqttClientMessage;
import com.dream.iot.client.mqtt.message.MqttMessageHead;
import com.dream.iot.test.TestProtocolType;
import com.dream.iot.test.TestStatus;
import com.dream.iot.test.TestStatusHeader;
import com.dream.iot.utils.ByteUtil;
import io.netty.handler.codec.mqtt.MqttQoS;

public class MqttClientTestMessage extends MqttClientMessage {

    public MqttClientTestMessage(byte[] message) {
        super(message);
    }

    public MqttClientTestMessage(MqttMessageHead head, String topic) {
        super(head, topic);
    }

    public MqttClientTestMessage(MqttMessageHead head, MqttQoS qos, String topic) {
        super(head, qos, topic);
    }

    public MqttClientTestMessage(MqttMessageHead head, MessageBody body, String topic) {
        super(head, body, topic);
    }

    @Override
    protected MqttMessageHead doBuild(byte[] payload) {
        if(this.getTopic().contains("willTopic")) {
            String clientId = this.getTopic().split("/")[1];
            return new MqttMessageHead(clientId, clientId, TestProtocolType.WillTop);
        } else if(this.getTopic().endsWith("Once")) {
            JSONObject jsonObject = JSONUtil.parseObj(ByteUtil.bytesToString(payload));
            return new MqttMessageHead(jsonObject.getStr("equipCode")
                    , jsonObject.getStr("messageId"), TestProtocolType.PIReq);
        } else if(this.getTopic().equals(MqttClientTestHandle.TOPIC_RESPONSE)) {
            JSONObject jsonObject = JSONUtil.parseObj(ByteUtil.bytesToString(payload));
            return new MqttMessageHead(jsonObject.getStr("equipCode")
                    , jsonObject.getStr("messageId"), TestProtocolType.CIReq);
        } else {
            JSONObject jsonObject = JSONUtil.parseObj(ByteUtil.bytesToString(payload));
            TestProtocolType type = jsonObject.get("type", TestProtocolType.class);
            if(type == TestProtocolType.PIReq) {
                return new TestStatusHeader(jsonObject.getStr("equipCode"),
                        jsonObject.getStr("messageId"), type, jsonObject.get("status", TestStatus.class));
            } else {
                return new MqttMessageHead(jsonObject.getStr("equipCode"), jsonObject.getStr("messageId"), type);
            }
        }
    }

}
