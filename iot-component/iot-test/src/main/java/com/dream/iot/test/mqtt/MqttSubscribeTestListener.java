package com.dream.iot.test.mqtt;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dream.iot.client.ClientComponent;
import com.dream.iot.client.IotClientBootstrap;
import com.dream.iot.client.mqtt.impl.DefaultMqttMessage;
import com.dream.iot.client.mqtt.impl.DefaultMqttPublishProtocol;
import com.dream.iot.client.mqtt.impl.DefaultMqttSubscribeProtocol;
import com.dream.iot.client.mqtt.impl.MqttSubscribeListener;
import com.dream.iot.client.mqtt.message.MqttMessageHead;
import com.dream.iot.test.TestConst;
import com.dream.iot.test.TestProtocolType;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttTopicSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@ConditionalOnExpression("${iot.test.client:false} and ${iot.test.mqtt.start:false}")
public class MqttSubscribeTestListener implements MqttSubscribeListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private MqttTopicSubscription subscription = new MqttTopicSubscription("dream/test/iot/+/+", MqttQoS.AT_MOST_ONCE);

    @Override
    public MqttTopicSubscription topic() {
        return subscription;
    }

    @Override
    public void onSubscribe(DefaultMqttSubscribeProtocol protocol) {
        DefaultMqttMessage mqttMessage = protocol.requestMessage();
        ClientComponent component = IotClientBootstrap.getClientComponent(DefaultMqttMessage.class);
        JSONObject jsonObject = JSONUtil.parseObj(new String(mqttMessage.getMessage()));
        if(jsonObject.containsKey("equipCode")) {
            logger.info(TestConst.LOGGER_MQTT_PROTOCOL_DESC, component.getName()
                    , "subscribe", protocol.getTopic(), protocol.getEquipCode(), "-", "通过" );

            String equipCode = jsonObject.getStr("equipCode");
            String messageId = jsonObject.getStr("messageId");
            MqttMessageHead messageHead = new MqttMessageHead(equipCode, messageId, TestProtocolType.CIReq);
            new DefaultMqttPublishProtocol(JSONUtil.toJsonStr(messageHead).getBytes(StandardCharsets.UTF_8)
                    , MqttClientTestHandle.TOPIC_RESPONSE+"/"+equipCode).request();
        }
    }
}
