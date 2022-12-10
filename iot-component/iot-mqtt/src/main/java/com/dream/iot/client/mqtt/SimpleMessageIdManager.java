package com.dream.iot.client.mqtt;

import com.dream.iot.ConcurrentStorageManager;
import com.dream.iot.client.mqtt.message.MqttClientMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleMessageIdManager extends ConcurrentStorageManager<Object, MessageMapper> implements MessageIdManager<Object>{

    /**
     * 重发周期(毫秒)
     */
    private int rate;
    /**
     * 重发多少次之后移除报文且不在重发(默认直接移除)
     */
    private int count;
    private MqttClient client;
    private MqttClientComponent clientComponent;
    private AtomicInteger messageId = new AtomicInteger(1);
    private static Logger logger = LoggerFactory.getLogger(SimpleMessageIdManager.class);


    public SimpleMessageIdManager(MqttClient client, MqttClientComponent clientComponent) {
        this(30000, 0, client, clientComponent);
    }

    public SimpleMessageIdManager(int rate, int count, MqttClient client, MqttClientComponent clientComponent) {
        this.rate = rate;
        this.count = count;
        this.client = client;
        this.clientComponent = clientComponent;
    }

    @Override
    public synchronized int nextId() {
        int andIncrement = messageId.getAndIncrement();
        if(andIncrement >= 0xffff) {
            messageId.set(1);
        }

        /**
         * 这里需要验证此PacketId是否可以使用
         */
        if(isExists(CLIENT_PREFIX+andIncrement)) {
            return nextId();
        }

        return andIncrement;
    }

    @Override
    public void expire() {
        final Iterator<Map.Entry<Object, MessageMapper>> iterator = getMapper().entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<Object, MessageMapper> entry = iterator.next();
            MessageMapper message = entry.getValue();
            final long expire = System.currentTimeMillis() - message.getLastTime();

            // 三十秒后重新发送
            if(expire >= this.getRate()) {

                // 重发3次然后校验一次是否移除此报文
                if(message.getCount() >= this.getCount()) {
                    // 服务端没有确认报文, 判断是否需要移除
                    if(clientComponent.getPublishListener().remove(client, message)) {
                        iterator.remove(); break;
                    }
                }

                // 重新发送报文
                if(client != null) {
                    message.inc(); // 递增重发次数
                    message.setLastTime(System.currentTimeMillis()); // 修改重发时间

                    final MqttClientMessage clientMessage = message.getMessage();
                    final MqttPublishMessage mqttMessage = client
                            .buildPublishDupMessage(clientMessage, message.getPacketId());

                    client.getChannel().writeAndFlush(mqttMessage).addListener(future -> {
                        final int packetId = mqttMessage.variableHeader().packetId();
                        if(future.isSuccess()) {
                            if(logger.isDebugEnabled()) {
                                logger.debug("mqtt({}) {}(重发成功) - PacketId：{} - 远程主机：{} - 重发次数：{}"
                                        , clientComponent.getName(), MqttMessageType.PUBLISH
                                        , packetId, message.getProperties(), message.getCount());
                            } else {
                                logger.warn("mqtt({}) {}(重发失败) - PacketId：{} - 远程主机：{} - 重发次数：{}"
                                        , clientComponent.getName(), MqttMessageType.PUBLISH
                                        , packetId, message.getProperties(), message.getCount(), future.cause());
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public MessageMapper get(Object packetId) {
        return super.get(CLIENT_PREFIX + packetId);
    }

    @Override
    public MessageMapper remove(Object packetId) {
        return super.remove(CLIENT_PREFIX+packetId);
    }

    @Override
    public MessageMapper add(Object key, MessageMapper val) {
        return super.add(CLIENT_PREFIX+key, val);
    }

    @Override
    public MessageMapper getServer(Integer packetId) {
        return super.get(SERVER_PREFIX+packetId);
    }

    @Override
    public MessageMapper addServer(Integer packetId, MessageMapper mapper) {
        return super.add(SERVER_PREFIX+packetId, mapper);
    }

    @Override
    public MessageMapper removeServer(Integer packetId) {
        return super.remove(SERVER_PREFIX+packetId);
    }

    public int getRate() {
        return rate;
    }

    public int getCount() {
        return count;
    }
}
