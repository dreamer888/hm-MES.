package com.dream.iot.client.mqtt;

import com.dream.iot.client.ClientConnectProperties;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttVersion;
import org.springframework.util.StringUtils;

/**
 * mqtt客户端连接配置
 */
public class MqttConnectProperties extends ClientConnectProperties {

    /**
     * clientId 用来标识唯一的连接
     */
    private String clientId;
    /**
     * 用户名
     * @see org.springframework.lang.Nullable
     */
    private String username;
    /**
     * 密码
     * @see org.springframework.lang.Nullable
     */
    private String password;
    /**
     * 遗嘱主题
     * 客户端断开后 将发送此主题
     * @see org.springframework.lang.Nullable
     */
    private String willTopic;
    /**
     * 遗嘱主题对应的消息
     * @see org.springframework.lang.Nullable
     */
    private String willMessage;
    /**
     * 遗嘱消息被发布时需要保留
     */
    private boolean willRetain;
    /**
     * cleanSession
     * 如果清理会话（CleanSession）标志被设置为0，服务端必须基于当前会话（使用客户端标识符识别）的状态恢复与客户端的通信。
     * 如果清理会话（CleanSession）标志被设置为1，客户端和服务端必须丢弃之前的任何会话并开始一个新的会话。会话仅持续和网络连接同样长的时间，即网络连接断开，会话结束。与这个会话关联的状态数据不能被任何之后的会话重用
     */
    private boolean cleanSession;
    /**
     * 发布遗嘱消息时使用的服务质量等级
     */
    private MqttQoS willQos = MqttQoS.AT_MOST_ONCE;
    /**
     * 版本(默认 3.1.1)
     */
    private MqttVersion version = MqttVersion.MQTT_3_1_1;

    /**
     * 使用本地和默认端口已经默认客户端id创建
     */
    public MqttConnectProperties(String clientId) {
        this("127.0.0.1", 1883, clientId);
    }

    /**
     * 使用本地和默认端口已经默认客户端id创建
     */
    public MqttConnectProperties(String remoteHost, String clientId) {
        this(remoteHost, 1883, clientId);
    }

    /**
     * 使用指定的客户端id创建
     * @param remoteHost
     * @param remotePort
     * @param clientId
     */
    public MqttConnectProperties(String remoteHost, Integer remotePort, String clientId) {
        super(remoteHost, remotePort, clientId);
        this.clientId = clientId;
        if(!StringUtils.hasText(this.clientId)) {
            throw new MqttClientException("clientId不能为空");
        }
    }

    public MqttConnectProperties(String remoteHost, Integer remotePort
            , String localHost, Integer localPort, String clientId) {
        super(remoteHost, remotePort, localHost, localPort);
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWillTopic() {
        return willTopic;
    }

    public void setWillTopic(String willTopic) {
        this.willTopic = willTopic;
    }

    public String getWillMessage() {
        return willMessage;
    }

    public void setWillMessage(String willMessage) {
        this.willMessage = willMessage;
    }

    public boolean isWillRetain() {
        return willRetain;
    }

    public void setWillRetain(boolean willRetain) {
        this.willRetain = willRetain;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public MqttQoS getWillQos() {
        return willQos;
    }

    public void setWillQos(MqttQoS willQos) {
        this.willQos = willQos;
    }

    public MqttVersion getVersion() {
        return version;
    }

    public void setVersion(MqttVersion version) {
        this.version = version;
    }
}
