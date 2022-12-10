package com.dream.iot.test.mqtt;

import com.dream.iot.client.ClientProtocolHandle;
import com.dream.iot.client.mqtt.gateway.MqttGatewayConnectProperties;
import com.dream.iot.client.mqtt.gateway.adapter.MqttGatewayJsonHandle;
import com.dream.iot.test.IotTestProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqttGatewayTestHandle implements ClientProtocolHandle<MqttSubscribeTestProtocol>
        , MqttGatewayJsonHandle<MqttSubscribeTestProtocol, Object>, InitializingBean {

    @Autowired
    private IotTestProperties iotTestProperties;
    private MqttGatewayConnectProperties properties;

    @Override
    public Object handle(MqttSubscribeTestProtocol protocol) {
        return new MqttGatewayEntity();
    }

    @Override
    public MqttGatewayConnectProperties getProperties(Object entity) {
        return this.properties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        IotTestProperties.TestMqttConnectProperties mqtt = iotTestProperties.getMqtt();
        if(mqtt != null) {
            this.properties = new MqttGatewayConnectProperties(mqtt.getHost(), mqtt.getPort(), "MqttGatewayTestClientId", "/mqtt/gateway");
        }
    }
}
