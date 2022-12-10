package com.dream.iot.client.mqtt;

import com.dream.iot.client.mqtt.gateway.MqtGatewayPublishListener;
import com.dream.iot.client.mqtt.gateway.MqttGatewayComponent;
import com.dream.iot.client.mqtt.gateway.MqttGatewayHandle;
import com.dream.iot.client.mqtt.gateway.MqttGatewayProxyMatcher;
import com.dream.iot.client.mqtt.impl.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.util.List;

@EnableConfigurationProperties(DefaultMqttConnectProperties.class)
public class IotMqttAutoConfiguration {

    @Bean
    @ConditionalOnBean({MqttSubscribeListener.class})
    @ConditionalOnMissingBean(DefaultMqttComponent.class)
    public DefaultMqttComponent defaultMqttComponent(DefaultMqttConnectProperties properties) {
        return new DefaultMqttComponent(properties);
    }

    @Bean
    @ConditionalOnBean(MqttSubscribeListener.class)
    @ConditionalOnMissingBean(DefaultMqttSubscribeHandle.class)
    public DefaultMqttSubscribeHandle defaultMqttSubscribeHandle(MqttSubscribeListenerManager listenerManager) {
        return new DefaultMqttSubscribeHandle(listenerManager);
    }

    @Bean
    @ConditionalOnBean(MqttSubscribeListener.class)
    @ConditionalOnMissingBean(MqttSubscribeListenerManager.class)
    public MqttSubscribeListenerManager mqttSubscribeListenerManager(List<MqttSubscribeListener> listeners) {
        return new MqttSubscribeListenerManager(listeners);
    }

    @Bean
    @ConditionalOnBean(MqttGatewayHandle.class)
    public MqttGatewayComponent mqttGatewayComponent() {
        return (MqttGatewayComponent) new MqttGatewayComponent()
                .setPublishListener(new MqtGatewayPublishListener());
    }

    @Bean
    @Order(10000)
    @ConditionalOnBean(MqttGatewayHandle.class)
    @ConditionalOnMissingBean(MqttGatewayProxyMatcher.class)
    public MqttGatewayProxyMatcher mqttGatewayProxyMatcher() {
        return new MqttGatewayProxyMatcher();
    }
}
