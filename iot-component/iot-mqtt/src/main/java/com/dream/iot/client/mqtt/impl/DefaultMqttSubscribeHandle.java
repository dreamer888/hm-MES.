package com.dream.iot.client.mqtt.impl;

import com.dream.iot.client.ClientProtocolHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class DefaultMqttSubscribeHandle implements ClientProtocolHandle<DefaultMqttSubscribeProtocol> {

    private MqttSubscribeListenerManager listenerManager;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public DefaultMqttSubscribeHandle(MqttSubscribeListenerManager listenerManager) {
        this.listenerManager = listenerManager;
    }

    @Override
    public Object handle(DefaultMqttSubscribeProtocol protocol) {
        try {
            // 匹配对应的监听器
            List<MqttSubscribeListener> listeners = listenerManager.matcher(protocol.getTopic());
            if(!CollectionUtils.isEmpty(listeners)) {
                listeners.forEach(listener -> listener.onSubscribe(protocol)); // 执行所有已经匹配的监听
            } else {
                logger.warn("客户端(MQTT<默认>) 未找到匹配的[MqttSubscribeListener] - topic：{} - qos：{}", protocol.getTopic(), protocol.getQoS());
            }
        } catch (Exception e) {
            logger.error("客户端(MQTT<默认>) 事件处理异常 - topic：{} - qos：{}", protocol.getTopic(), protocol.getQoS(), e);
        }

        return null;
    }
}
