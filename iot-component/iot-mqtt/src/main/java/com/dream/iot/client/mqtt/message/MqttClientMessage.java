package com.dream.iot.client.mqtt.message;

import com.dream.iot.client.ClientMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;

/**
 * 基于mqtt协议的客户端报文, 用于mqtt publish类型
 */
public abstract class MqttClientMessage extends ClientMessage {

    /**
     * @see MqttQoS#AT_MOST_ONCE 服务端将不会确认, 质量最差
     * @see MqttQoS#AT_LEAST_ONCE 服务端将会有一次确认
     * @see MqttQoS#EXACTLY_ONCE 服务端将会有三次确认 等级最高, 保证发布的报文有且只会发布一次
     */
    private MqttQoS qos;

    /**
     * 要发布的订阅主题
     * 支持通配符：# +
     */
    private String topic;

    /**
     * 表示服务器要保留这次推送的信息，如果有新的订阅者出现，就把这消息推送给它。如果不设那么推送至当前订阅的就释放了
     * 注：新订阅者，只会取出最新的一个RETAIN flag = 1的消息推送，不是所有
     */
    private boolean retained;

    /**
     * 源mqtt报文
     */
    private MqttPublishMessage mqttMessage;

    public MqttClientMessage(byte[] message) {
        super(message);
    }

    /**
     * 注：此构造函数必须存在于子类中
     */
    public MqttClientMessage(MqttPublishMessage message) {
        super(EMPTY);
        this.mqttMessage = message;
    }

    public MqttClientMessage(MqttMessageHead head, String topic) {
        this(head, VOID_MESSAGE_BODY, MqttQoS.AT_MOST_ONCE, topic);
    }

    public MqttClientMessage(MqttMessageHead head, MqttQoS qos, String topic) {
        super(head);
        this.qos = qos;
        this.topic = topic;
    }

    public MqttClientMessage(MqttMessageHead head, MessageBody body, String topic) {
        this(head, body, MqttQoS.AT_MOST_ONCE, topic);
    }

    public MqttClientMessage(MqttMessageHead head, MessageBody body, MqttQoS qos, String topic) {
        super(head, body);
        this.qos = qos;
        this.topic = topic;
    }

    @Override
    public MqttClientMessage readBuild() {
        this.qos = this.mqttMessage.fixedHeader().qosLevel();
        this.topic = this.mqttMessage.variableHeader().topicName();
        this.retained = this.mqttMessage.fixedHeader().isRetain();
        return (MqttClientMessage) super.readBuild();
    }

    @Override
    protected abstract MqttMessageHead doBuild(byte[] payload);

    @Override
    public MqttMessageHead getHead() {
        return (MqttMessageHead) super.getHead();
    }

    public String getTopic() {
        return topic;
    }

    public MqttClientMessage topic(String topic) {
        this.topic = topic;
        return this;
    }

    public MqttQoS getQos() {
        return qos;
    }

    public MqttClientMessage qos(MqttQoS qos) {
        this.qos = qos;
        return this;
    }

    public boolean isRetained() {
        return retained;
    }

    public MqttClientMessage retained(boolean retained) {
        this.retained = retained;
        return this;
    }

    public MqttPublishMessage getMqttMessage() {
        return mqttMessage;
    }

    public MqttClientMessage setMqttMessage(MqttPublishMessage mqttMessage) {
        this.mqttMessage = mqttMessage;
        return this;
    }
}
