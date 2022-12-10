package com.dream.iot.client.mqtt;

import com.dream.iot.client.mqtt.message.MqttClientMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttQoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create time: 2021/9/4
 *  Mqtt Publish 类型移除时的回调监听
 *  注意：不能在此回调执行耗时的操作, 会占用Netty工作线程
 * @author dream
 * @since 1.0
 */
public interface MessagePublishListener {

    MessagePublishListener LOGGER_LISTENER = new LoggerListener();

    /**
     * 在一直没有确认的情况下移除时调用(默认一直发送)
     *  如果没有正确收到服务端的确认报文则作如下判断：
     *      1. 返回 true, 直接移除该PUBLISH报文
     *      2. 返回 false, 不移除报文, 重置过期时间和重发次数, 继续重发
     * @see MessageIdManager#expire()
     * @param mapper
     */
    default boolean remove(MqttClient client, MessageMapper mapper) {
        return true;
    }

    /**
     * 在服务端确认完之后移除时调用
     * @see io.netty.handler.codec.mqtt.MqttQoS#AT_LEAST_ONCE {@link io.netty.handler.codec.mqtt.MqttMessageType#PUBACK}
     * @see io.netty.handler.codec.mqtt.MqttQoS#EXACTLY_ONCE {@link io.netty.handler.codec.mqtt.MqttMessageType#PUBCOMP}
     * @param mapper
     */
    void success(MqttClient client, MessageMapper mapper);

    class LoggerListener implements MessagePublishListener {

        private Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public boolean remove(MqttClient client, MessageMapper mapper) {
            final MqttClientMessage message = mapper.getMessage();
            MqttMessageType type = message.getQos() == MqttQoS.EXACTLY_ONCE
                    ? MqttMessageType.PUBCOMP : MqttMessageType.PUBACK;

            final int packetId = mapper.getPacketId();
            logger.warn("mqtt({}) 报文没有确认处理({}) - PacketId：{} - 远程主机：{} - 处理：直接移除"
                    , client.getName(), type, packetId, client.getConfig());

            return true;
        }

        @Override
        public void success(MqttClient client, MessageMapper mapper) {

        }
    }
}
