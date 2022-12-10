package com.dream.iot.test.mqtt;

import com.dream.iot.client.ClientProtocolHandle;
import com.dream.iot.client.mqtt.MqttClient;
import com.dream.iot.client.mqtt.MqttConnectProperties;
import com.dream.iot.test.IotTestHandle;
import com.dream.iot.test.IotTestProperties;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.TimeUnit;

/**
 * create time: 2021/9/3
 *
 * @author dream
 * @since 1.0
 */
public class MqttClientTestHandle implements ClientProtocolHandle<MqttPublishTestProtocol>, IotTestHandle {

    @Autowired
    private IotTestProperties properties;
    @Autowired
    private ThreadPoolTaskScheduler scheduler;
    @Autowired
    private MqttClientTestComponent component;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final String TOPIC_RESPONSE = "dream/test/cus/response";
    public static final String AT_MOST_ONCE_TOPIC = "dream/test/iot/atMostOnce/0";
    public static final String EXACTLY_ONCE_TOPIC = "dream/test/iot/exactlyOnce/2";
    public static final String AT_LEAST_ONCE_TOPIC = "dream/test/iot/atLeastOnce/1";

    @Override
    public Object handle(MqttPublishTestProtocol protocol) {
        return null;
    }

    @Override
    public void start() throws Exception{
        IotTestProperties.TestMqttConnectProperties config = properties.getMqtt();

        IotTestProperties.TestMqttConnectProperties willTopicConfig = new IotTestProperties.TestMqttConnectProperties();
        // 保留遗嘱的测试客户端
        BeanUtils.copyProperties(config, willTopicConfig, "clientId", "deviceSn");
        willTopicConfig.setWillTopic("dream/willTopic/iot/"+willTopicConfig.getClientId());
        willTopicConfig.setWillRetain(false); // 不保留的遗嘱
        willTopicConfig.setWillQos(MqttQoS.AT_LEAST_ONCE);
        willTopicConfig.setWillMessage("{\"retain\": false}"); // 不保留遗嘱测试
        MqttClient retainWillClient = component.createNewClientAndConnect(willTopicConfig);

        MqttConnectProperties willRetainTopicConfig = new IotTestProperties.TestMqttConnectProperties();
        // 不保留遗嘱的测试客户端
        BeanUtils.copyProperties(config, willRetainTopicConfig, "clientId", "deviceSn");
        willRetainTopicConfig.setWillTopic("dream/willTopic/iot/"+willRetainTopicConfig.getClientId());
        willRetainTopicConfig.setWillRetain(true); // 保留的遗嘱
        willRetainTopicConfig.setWillQos(MqttQoS.AT_MOST_ONCE);
        willRetainTopicConfig.setWillMessage("{\"retain\": true}"); // 保留遗嘱测试
        MqttClient willClient = component.createNewClientAndConnect(willRetainTopicConfig);

        System.out.println("---------------------------------------------------- 开始mqtt测试 ----------------------------------------------------------");

        willClient.disconnect(true); // 断线重连测试
        retainWillClient.disconnect(false); // 断线重连测试

        // 测试最多发送一次报文
        new MqttPublishTestProtocol(MqttQoS.AT_MOST_ONCE, AT_MOST_ONCE_TOPIC, willTopicConfig.getDeviceSn()).request(willTopicConfig);

        // 最少发送一次报文
        new MqttPublishTestProtocol(MqttQoS.AT_LEAST_ONCE, AT_LEAST_ONCE_TOPIC, willTopicConfig.getDeviceSn()).request(willTopicConfig);

        // 确保一定发送一次
        new MqttPublishTestProtocol(MqttQoS.EXACTLY_ONCE, EXACTLY_ONCE_TOPIC, willTopicConfig.getDeviceSn()).request(willTopicConfig);

        TimeUnit.SECONDS.sleep(3);
    }

    @Override
    public int getOrder() {
        return 1000 * 30;
    }
}
