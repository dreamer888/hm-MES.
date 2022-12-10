package com.dream.iot.client.mqtt;

import com.dream.iot.client.mqtt.message.MqttClientMessage;

public class MessageMapper {

    /**
     * 发送次数
     */
    private int count;
    /**
     * 最后一次发送时间
     */
    private long lastTime;

    /**
     * packetId
     */
    private int packetId;

    private MqttClientMessage message;
    private MqttConnectProperties properties;

    public MessageMapper(MqttConnectProperties properties, MqttClientMessage message, int packetId) {
        this.message = message;
        this.packetId = packetId;
        this.properties = properties;
        this.lastTime = System.currentTimeMillis();
    }

    /**
     * 发送次数 +1
     * @return
     */
    public MessageMapper inc() {
        this.count ++;
        return this;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public MqttClientMessage getMessage() {
        return message;
    }

    public MessageMapper setMessage(MqttClientMessage message) {
        this.message = message;
        return this;
    }

    public MqttConnectProperties getProperties() {
        return properties;
    }

    public MessageMapper setProperties(MqttConnectProperties properties) {
        this.properties = properties;
        return this;
    }

    public int getCount() {
        return count;
    }

    public MessageMapper setCount(int count) {
        this.count = count;
        return this;
    }

    public int getPacketId() {
        return packetId;
    }
}
