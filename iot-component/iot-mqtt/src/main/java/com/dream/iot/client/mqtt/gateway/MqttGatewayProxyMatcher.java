package com.dream.iot.client.mqtt.gateway;

import com.alibaba.fastjson.JSONObject;
import com.dream.iot.client.mqtt.MqttClientException;
import com.dream.iot.client.mqtt.gateway.adapter.MqttGatewayByteHandle;
import com.dream.iot.client.mqtt.gateway.adapter.MqttGatewayJsonHandle;
import com.dream.iot.client.mqtt.message.MqttMessageHead;
import com.dream.iot.handle.proxy.ProtocolHandleInvocationHandler;
import com.dream.iot.handle.proxy.ProtocolHandleProxyMatcher;
import com.dream.iot.message.DefaultMessageBody;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.springframework.util.StringUtils;

public class MqttGatewayProxyMatcher implements ProtocolHandleProxyMatcher {

    @Override
    public boolean matcher(Object target) {
        return target instanceof MqttGatewayHandle;
    }

    @Override
    public ProtocolHandleInvocationHandler invocationHandler(Object target) {
        return new ProtocolHandleInvocationHandler(target) {

            @Override
            public Class<MqttGatewayHandle> getProxyClass() {
                return MqttGatewayHandle.class;
            }

            @Override
            protected Object proxyHandle(Object value, Object proxy) {
                final MqttGatewayConnectProperties properties = getTarget().getProperties(value);
                if(properties == null) {
                    throw new MqttClientException("请返回正确的mqtt配置[MqttGatewayConnectProperties]");
                }
                final MqttQoS qoS = properties.getQoS();
                final String topic = properties.getTopic();
                final MqttMessageHead mqttMessageHead = getTarget().getMqttGatewayHead(value);
                final MqttGatewayMessage gatewayMessage = new MqttGatewayMessage(mqttMessageHead, qoS, topic);

                /**
                 * 发布报文
                 */
                if(proxy instanceof MqttGatewayJsonHandle) {
                    final byte[] jsonBytes = JSONObject.toJSONBytes(value);
                    gatewayMessage.setBody(new DefaultMessageBody(jsonBytes));
                } else if(proxy instanceof MqttGatewayByteHandle) {
                    gatewayMessage.setBody(new DefaultMessageBody((byte[]) value));
                } else {
                    throw new MqttClientException("不支持的处理器["+getTarget().getClass().getSimpleName()+"]");
                }

                if(!StringUtils.hasText(topic)) {
                    throw new MqttClientException("未指定Mqtt要发布的topic在配置[MqttGatewayConnectProperties]");
                }

                // 发送请求
                new MqttGatewayProtocol(gatewayMessage).request(properties);
                return value;
            }

            @Override
            public MqttGatewayHandle getTarget() {
                return (MqttGatewayHandle) super.getTarget();
            }
        };
    }

}
