package com.dream.iot.test.mqtt;

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

/**
 * topic模式匹配测试
 */
@Component
@ConditionalOnExpression("${iot.test.client:false} and ${iot.test.mqtt.start:false}")
public class MqttPatternTestListener implements MqttSubscribeListener {

    private String pattern;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public MqttTopicSubscription topic() {
        this.pattern = "dream/test/iot/+/#";
        return new MqttTopicSubscription(this.pattern, MqttQoS.AT_MOST_ONCE);
    }

    @Override
    public void onSubscribe(DefaultMqttSubscribeProtocol protocol) {
        ClientComponent component = IotClientBootstrap.getClientComponent(DefaultMqttMessage.class);
        logger.info(TestConst.LOGGER_MQTT_PROTOCOL_DESC, component.getName()
                , "模式匹配("+this.pattern+")", protocol.getTopic(), protocol.getEquipCode(), "-", "通过" );
    }
}
