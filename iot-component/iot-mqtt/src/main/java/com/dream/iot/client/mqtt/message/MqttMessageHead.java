package com.dream.iot.client.mqtt.message;

import com.dream.iot.ProtocolType;
import com.dream.iot.message.DefaultMessageHead;

public class MqttMessageHead extends DefaultMessageHead {

    public MqttMessageHead(byte[] message) {
        super(message);
    }

    /**
     * 使用自定义的messageId
     * @param equipCode
     * @param messageId 自定义MessageId
     * @param type
     */
    public MqttMessageHead(String equipCode, String messageId, ProtocolType type) {
        super(equipCode, messageId, type);
    }
}
