package com.dream.iot.test.mqtt;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.dream.iot.client.ClientComponent;
import com.dream.iot.client.IotClientBootstrap;
import com.dream.iot.client.mqtt.impl.DefaultMqttMessage;
import com.dream.iot.client.mqtt.impl.DefaultMqttSubscribeProtocol;
import com.dream.iot.client.mqtt.impl.MqttSubscribeListener;
import com.dream.iot.test.TestConst;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttTopicSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("${iot.test.client:false} and ${iot.test.mqtt.start:false}")
public class MqttWillTopicTestListener implements MqttSubscribeListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public MqttTopicSubscription topic() {
        return new MqttTopicSubscription("dream/willTopic/iot/#", MqttQoS.EXACTLY_ONCE);
    }

    @Override
    public void onSubscribe(DefaultMqttSubscribeProtocol protocol) {
        byte[] message = protocol.requestMessage().getMessage();
        ClientComponent component = IotClientBootstrap.getClientComponent(DefaultMqttMessage.class);
        JSONObject jsonObject = JSONUtil.parseObj(new String(message));
        if(jsonObject.containsKey("retain")) {
            logger.info(TestConst.LOGGER_MQTT_PROTOCOL_DESC, component.getName()
                    , "WillTopic", protocol.getTopic(), protocol.getEquipCode(), "-", "通过" );
        }
    }
}
