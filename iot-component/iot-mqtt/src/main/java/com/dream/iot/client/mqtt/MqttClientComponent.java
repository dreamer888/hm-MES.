package com.dream.iot.client.mqtt;

import com.dream.iot.SocketMessage;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.MultiClientManager;
import com.dream.iot.client.component.TcpClientComponent;
import com.dream.iot.client.mqtt.message.MqttClientMessage;
import com.dream.iot.config.ConnectProperties;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 基于mqtt协议的客户端组件
 * @see MqttClient
 * @param <M>
 */
public abstract class MqttClientComponent<M extends MqttClientMessage> extends TcpClientComponent<M> {


    private Logger logger = LoggerFactory.getLogger(MqttClientComponent.class);
    private MessagePublishListener publishListener = MessagePublishListener.LOGGER_LISTENER;

    public MqttClientComponent() { }

    /**
     * @param config 默认客户端配置
     */
    public MqttClientComponent(MqttConnectProperties config) {
        super(config);
    }

    public MqttClientComponent(MqttConnectProperties config, MultiClientManager clientManager) {
        super(config, clientManager);
    }

    public MqttClientComponent(ClientConnectProperties config, MultiClientManager clientManager, MessagePublishListener publishListener) {
        super(config, clientManager);
        this.publishListener = publishListener;
    }

    /**
     * 此处不进行构建
     * @see MqttClient#buildPublishMessage(ChannelHandlerContext, MqttPublishMessage) 移到此处构建
     * @param message
     * @return
     */
    @Override
    public SocketMessage readBuild(SocketMessage message) {
        return message;
    }

    @Override
    public SocketMessage createMessage(byte[] message) {
        throw new UnsupportedOperationException("请使用方法createMessage(MqttPublishMessage)替代");
    }

    @Override
    public MqttClient createNewClient(ClientConnectProperties config) {
        return new MqttClient(this, config);
    }

    @Override
    public MqttClient createNewClientAndConnect(ClientConnectProperties config) {
        return (MqttClient) super.createNewClientAndConnect(config);
    }

    @Override
    public void init(Object... args) {
        super.init(args);

        // 定时重发处理
        resendMsgSchedule((NioEventLoopGroup) args[0]);
    }

    protected void resendMsgSchedule(NioEventLoopGroup arg) {
        // 十秒处理一次
        arg.scheduleAtFixedRate(() -> {
            try {
                this.clients().forEach(item -> {
                    MqttClient mqttClient = (MqttClient) item;
                    // 对所有还在线的客户端进行报文重发和移除处理
                    if(mqttClient.getChannel().isActive()) {
                        mqttClient.getMessageIdManager().expire();
                    }
                });
            } catch (Exception e) {
                logger.error("mqtt客户端重发错误", e);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    /**
     * 发送ping请求
     * @param remote 如果为空则使用{@link #getConfig()}默认地址
     * @return
     */
    public ChannelFuture ping(MqttConnectProperties... remote) {
        ConnectProperties clientKey = getConfig();
        if(remote != null) {
            clientKey = remote[0];
        }

        MqttClient client = getClient(clientKey);
        if(client != null) {
            return client.getChannel().writeAndFlush(MqttMessage.PINGREQ);
        } else {
            throw new IllegalStateException("获取不到客户端["+clientKey+"]");
        }
    }

    /**
     * 订阅主题
     * @param topic 主题
     * @param qoS 表示服务端向自己转发消息时可以使用的最大 QoS
     * @param remote 如果为空则使用{@link #getConfig()}默认地址
     * @return
     */
    public ChannelFuture subscribe(String topic, MqttQoS qoS, MqttConnectProperties... remote) {
        if(!StringUtils.hasText(topic)) {
            throw new IllegalArgumentException("[topic]必填");
        }

        ConnectProperties clientKey = getConfig();
        if(remote != null) {
            clientKey = remote[0];
        }

        MqttClient client = getClient(clientKey);
        if(client != null) {
            final MqttMessageBuilders.SubscribeBuilder subscription = MqttMessageBuilders.subscribe()
                    .messageId(client.getMessageIdManager().nextId()).addSubscription(qoS, topic);
            return client.getChannel().writeAndFlush(subscription);
        } else {
            throw new IllegalStateException("获取不到客户端["+clientKey+"]");
        }

    }

    /**
     * 取消订阅指定主题
     * @param topic 主题
     * @param remote 如果为空则使用{@link #getConfig()}默认地址
     * @return
     */
    public ChannelFuture unsubscribe(String topic, MqttConnectProperties... remote) {
        if(!StringUtils.hasText(topic)) {
            throw new IllegalArgumentException("[topic]必填");
        }

        ConnectProperties clientKey = getConfig();
        if(remote != null) {
            clientKey = remote[0];
        }

        MqttClient client = getClient(clientKey);
        if(client != null) {
            MqttUnsubscribeMessage unsubscribeMessage = MqttMessageBuilders.unsubscribe().addTopicFilter(topic).build();
            return client.getChannel().writeAndFlush(unsubscribeMessage);
        } else {
            throw new IllegalStateException("获取不到客户端["+clientKey+"]");
        }
    }

    /**
     * 新增报文订阅
     * @see MqttQoS 服务端发布给客户端在该topic中最大的qos
     * @param client 当前Mqtt客户端要订阅的配置
     */
    protected abstract List<MqttTopicSubscription> doSubscribe(MqttConnectProperties client);

    @Override
    public MqttClient getClient(Object clientKey) {
        return (MqttClient) super.getClient(clientKey);
    }

    /**
     * 新增发布报文监听
     * @param publishListener
     * @return
     */
    public MqttClientComponent<M> setPublishListener(MessagePublishListener publishListener) {
        this.publishListener = publishListener;
        return this;
    }

    public MessagePublishListener getPublishListener() {
        return publishListener;
    }

}
