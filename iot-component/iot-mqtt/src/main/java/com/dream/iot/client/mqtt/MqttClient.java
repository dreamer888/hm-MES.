package com.dream.iot.client.mqtt;

import com.dream.iot.*;
import com.dream.iot.client.*;
import com.dream.iot.client.mqtt.message.MqttClientMessage;
import com.dream.iot.client.protocol.ClientSocketProtocol;
import com.dream.iot.client.protocol.ServerInitiativeProtocol;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.event.ClientStatus;
import com.dream.iot.event.ClientStatusEvent;
import com.dream.iot.utils.ByteUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 基于mqtt协议的客户端
 * @see MqttConnectProperties#getWriterIdleTime() 作为keepAlive字段 单位毫秒
 * 详情资料{@code https://blog.csdn.net/weixin_40973138/article/details/90036953}
 * @see MqttEncoder
 * @see MqttDecoder
 */
public class MqttClient extends TcpSocketClient implements
        ChannelInboundHandler, ChannelOutboundHandler, MultiStageConnect {

    /**
     * 用来标识mqtt客户端是否已经得到服务端的connAck
     */
    private ChannelPromise connAckFinished;
    private MessageIdManager messageIdManager;

    public MqttClient(MqttClientComponent clientComponent) {
        this(clientComponent, clientComponent.getConfig());
    }

    public MqttClient(MqttClientComponent clientComponent, ClientConnectProperties config) {
        super(clientComponent, config);
        this.messageIdManager = new SimpleMessageIdManager(this, clientComponent);
    }

    public MqttClient(MqttClientComponent clientComponent, ClientConnectProperties config, MessageIdManager messageIdManager) {
        super(clientComponent, config);
        this.messageIdManager = messageIdManager;
    }

    @Override
    protected ChannelInboundHandler createProtocolDecoder() {
        return new MqttDecoder();
    }

    @Override
    protected ChannelOutboundHandlerAdapter createProtocolEncoder() {
        return MqttEncoder.INSTANCE;
    }

    /**
     * 新增MqttMessage到MqttClientMessage的处理器 包括心跳处理器和解码处理器
     * @param channel
     */
    @Override
    protected void doInitChannel(Channel channel) {
        if(getConfig().getReaderIdleTime() > 0 || getConfig().getReaderIdleTime() > 0
                || getConfig().getAllIdleTime() > 0) {
            // 心跳处理
            channel.pipeline().addLast(new IdleStateHandler(getConfig().getReaderIdleTime()
                    , getConfig().getWriterIdleTime(), getConfig().getAllIdleTime(), TimeUnit.MILLISECONDS));
        }

        channel.pipeline().addLast("MqttMessageToClientMessageHandler", this);
    }

    /**
     * 连接成功后发起 connect 请求
     * @param future
     */
    @Override
    protected void successCallback(ChannelFuture future) {
        boolean willFlag = getConfig().getWillMessage() != null && getConfig().getWillTopic() != null;
        // 写超时作为保活时间
        long seconds = TimeUnit.MILLISECONDS.toSeconds(getConfig().getWriterIdleTime());

        MqttConnectMessage connectMessage = MqttMessageBuilders.connect()
                .willFlag(willFlag)
                .willQoS(getConfig().getWillQos())
                .clientId(getConfig().getClientId())
                .username(getConfig().getUsername())
                .password(getConfig().getPassword())
                .willTopic(getConfig().getWillTopic())
                .willRetain(getConfig().isWillRetain())
                .willMessage(getConfig().getWillMessage())
                .protocolVersion(getConfig().getVersion())
                .cleanSession(getConfig().isCleanSession())
                .hasUser(getConfig().getUsername() != null)
                .keepAlive(Long.valueOf(seconds).intValue())
                .hasPassword(getConfig().getPassword() != null)
                .build();

        this.getChannel().writeAndFlush(connectMessage).addListener(future1 -> {
            if(future1.isSuccess()) {
                if(logger.isTraceEnabled()) {
                    logger.trace("mqtt({}) {} - 远程主机：{} - 状态：成功 - 报文：{}"
                            , getName(), MqttMessageType.CONNECT, remoteKey(), connectMessage);
                }
            } else {
                logger.warn("mqtt({}) {} - 远程主机：{} - 状态：失败 - 报文：{}"
                        , getName(), MqttMessageType.CONNECT, remoteKey(), connectMessage, future1.cause());
            }
        });
    }

    @Override
    public ChannelFuture disconnect(boolean remove) {
        MqttFixedHeader header = new MqttFixedHeader(MqttMessageType.DISCONNECT
                ,false, MqttQoS.AT_MOST_ONCE, false, 0);
        MqttMessage disconnectMqttMessage = new MqttMessage(header);

        // 发送断开连接报文
        return this.getChannel().writeAndFlush(disconnectMqttMessage).addListener(future -> {
            if(future.isSuccess()) {
                getChannel().close().addListener(disFuture -> {
                    if(disFuture.isSuccess()) {
                        disconnectSuccessCall(remove);
                        if(logger.isTraceEnabled()) {
                            logger.trace("mqtt({}) 关闭客户端({}) - 远程主机：{} - 状态：成功"
                                    , getName(), MqttMessageType.DISCONNECT, remoteKey());
                        }
                    } else {
                        logger.warn("mqtt({}) 关闭客户端({}) - 远程主机：{} - 状态：失败"
                                , getName(), MqttMessageType.DISCONNECT, remoteKey(), disFuture.cause());
                    }
                });
                if(logger.isTraceEnabled()) {
                    logger.trace("mqtt({}) {} - 远程主机：{} - 状态：成功"
                            , getName(), MqttMessageType.DISCONNECT, remoteKey());
                }
            } else if(future.cause() instanceof ClosedChannelException) { // 服务端已经关闭此连接, 也属于完成
                disconnectSuccessCall(remove);
                if(logger.isTraceEnabled()) {
                    logger.trace("mqtt({}) 关闭客户端({}) - 远程主机：{} - 状态：成功"
                            , getName(), MqttMessageType.DISCONNECT, remoteKey());
                }
            } else {
                logger.warn("mqtt({}) {} - 远程主机：{} - 状态：失败"
                        , getName(), MqttMessageType.DISCONNECT, remoteKey(), future.cause());
            }
        });
    }

    @Override
    public ChannelFuture writeAndFlush(ClientSocketProtocol clientProtocol) {
        ClientMessage clientMessage = clientProtocol.requestMessage();
        if(!(clientMessage instanceof MqttClientMessage)) {
            throw new ProtocolException("Mqtt报文必须使用类型[MqttClientMessage]");
        }

        return super.writeAndFlush(clientProtocol);
    }

    private Object debugMessage(ClientMessage message, MqttPublishMessage mqttMessage) {
        final byte[] binMsg = message.getMessage();
        final MqttFixedHeader fixedHeader = mqttMessage.fixedHeader();
        final MqttPublishVariableHeader variableHeader = mqttMessage.variableHeader();
        return new StringBuilder("MqttPublishMessage")
                .append('[')
                .append(fixedHeader != null ? fixedHeader.toString() : "").append(',')
                .append(variableHeader != null ? variableHeader.toString() : "")
                .append(", payload=[").append(binMsg != null ? ByteUtil.bytesToHex(binMsg) : "")
                .append("]]")
                .toString();
    }

    /**
     * 构建要发布的报文
     * @param message
     * @return
     * @throws IOException
     */
    public MqttPublishMessage buildPublishMqttMessage(MqttClientMessage message) {
        final int nextId = getMessageIdManager().nextId();

        return this.buildPublishMqttMessage(message, nextId);
    }

    protected MqttPublishMessage buildPublishMqttMessage(MqttClientMessage message, Integer packetId) {
        if(message.getMessage() == null) {
            message.writeBuild();
        }

        return MqttMessageBuilders.publish()
                .messageId(packetId)
                .qos(message.getQos())
                .topicName(message.getTopic())
                .retained(message.isRetained())
                .payload(Unpooled.wrappedBuffer(message.getMessage()))
                .build();
    }

    /**
     * 发送副本
     * @param message
     * @param packetId
     * @return
     */
    protected MqttPublishMessage buildPublishDupMessage(MqttClientMessage message, Integer packetId) {
        if(message.getMessage() == null) {
            message.writeBuild();
        }

        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, true
                , message.getQos(), message.isRetained(), 0);
        MqttPublishVariableHeader mqttVariableHeader = new MqttPublishVariableHeader(message.getTopic(), packetId);
        return new MqttPublishMessage(mqttFixedHeader, mqttVariableHeader, Unpooled.buffer().writeBytes(message.getMessage()));
    }

    protected String remoteKey() {
        return getConfig().getHost() + ":" + getConfig().connectKey();
    }

    @Override
    public MqttConnectProperties getConfig() {
        return (MqttConnectProperties) super.getConfig();
    }

    @Override
    public MqttClientComponent getClientComponent() {
        return (MqttClientComponent) super.getClientComponent();
    }

    public MessageIdManager getMessageIdManager() {
        return messageIdManager;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MqttMessage message = (MqttMessage) msg;
        MqttMessageType type = message.fixedHeader().messageType();

        switch (type) {
            // 连接确认
            case CONNACK: connAck((MqttConnAckMessage) msg, ctx); break;

            // 服务端 发布
            case PUBLISH: publish((MqttPublishMessage) msg, ctx); break;

            // 客户端发布确认(QoS1)
            case PUBACK: pubAck((MqttPubAckMessage) msg, ctx); break;

            // 客户端发布 第一次确认(QoS2)
            case PUBREC: pubRec(message, ctx); break;
            // 服务端发布 发布第二次确认(QoS2)
            case PUBREL: pubRel(message, ctx); break;
            // 客户端发布 发布第三次确认(QoS2)
            case PUBCOMP: pubComp(message, ctx); break;

            // 订阅确认
            case SUBACK: subAck((MqttSubAckMessage) msg, ctx); break;
            // 取消订阅确认
            case UNSUBACK: unSubAck((MqttUnsubAckMessage) msg, ctx); break;

            // ping 响应
            case PINGRESP: pingResp(message, ctx); break;
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            // 发送Ping请求
            ctx.channel().writeAndFlush(MqttMessage.PINGREQ);
        } else {
            ctx.fireUserEventTriggered(evt);
        }
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelWritabilityChanged();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public ChannelPromise getConnectFinishedFlag() {
        return this.connAckFinished;
    }

    @Override
    public MultiStageConnect setConnectFinishedFlag(ChannelPromise promise) {
        this.connAckFinished = promise;
        return this;
    }

    @Override
    public ChannelFuture doConnect(Consumer<ChannelFuture> consumer, long timeout) {
        return super.doConnect(consumer, timeout);
    }

    @Override
    public ChannelFuture connect(Consumer<?> consumer, long timeout) {
        return this.stageConnect(consumer == null ? a -> {} : consumer, timeout);
    }

    /**
     * 服务端的连接确认报文 确认之后我们将发起向服务端订阅的报文
     * @param msg
     * @param ctx
     */
    protected void connAck(MqttConnAckMessage msg, ChannelHandlerContext ctx) {
        MqttConnectReturnCode mqttConnectReturnCode = msg.variableHeader().connectReturnCode();
        // 连接成功, 发送订阅报文
        if(mqttConnectReturnCode == MqttConnectReturnCode.CONNECTION_ACCEPTED) {
            this.setSuccess(); // 标记已经成功
            MqttConnectProperties client = (MqttConnectProperties)ctx.channel().attr(CoreConst.CLIENT_KEY).get();
            List<MqttTopicSubscription> list = this.getClientComponent().doSubscribe(client);// 交由实现者新增订阅
            if(!CollectionUtils.isEmpty(list)) {
                int messageId = messageIdManager.nextId();
                MqttMessageBuilders.SubscribeBuilder subscribe = MqttMessageBuilders.subscribe();
                subscribe.messageId(messageId);

                list.forEach(item -> subscribe.addSubscription(item.qualityOfService(), item.topicName()));
                this.doWriteAndFlush(MqttMessageType.SUBSCRIBE, ctx.channel(), subscribe.build(), messageId);
            }

            // 发布客户端上线事件
            IotClientBootstrap.publishApplicationEvent(new ClientStatusEvent(this, ClientStatus.online, getClientComponent()));
        } else {
            this.setFailure(new MqttClientException("CONN_ACK失败：" + mqttConnectReturnCode));
            logger.warn("mqtt({}) {} - 状态: {}", getName(), MqttMessageType.CONNACK, mqttConnectReturnCode);
        }
    }

    /**
     * 服务端发布报文, 收到服务端发布的报文后, 解析出报文 payload 并组装成MqttClientMessage
     * @param msg
     * @param ctx
     */
    protected void publish(MqttPublishMessage msg, ChannelHandlerContext ctx) throws Exception {
        final MqttFixedHeader fixedHeader = msg.fixedHeader();
        final int packetId = msg.variableHeader().packetId();

        ConnectProperties properties = ctx.channel().attr(CoreConst.CLIENT_KEY).get();
        if(logger.isTraceEnabled()) {
            logger.trace("mqtt({}) {}(SERVER {}) - PacketId：{} - 主机：{}"
                    , getName(), MqttMessageType.PUBLISH, fixedHeader.qosLevel(), packetId, properties);
        }

        /**
         * 校验是否是服务端重复发送的报文
         * 1. 如果packetId的报文存在说明客户端已经收到了一次, 不能重复接受
         * 2. 直接返回确认报文
         */
        if(fixedHeader.qosLevel() != MqttQoS.AT_MOST_ONCE) {
            final MessageMapper server = getMessageIdManager().getServer(packetId);
            if(server != null) {
                final MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(packetId);
                MqttMessageType type = fixedHeader.qosLevel() == MqttQoS.AT_LEAST_ONCE
                        ? MqttMessageType.PUBACK : MqttMessageType.PUBREC;
                final MqttFixedHeader ackFixedHeader = new MqttFixedHeader(type
                        , false, MqttQoS.AT_MOST_ONCE, false, 0);

                getChannel().writeAndFlush(new MqttMessage(ackFixedHeader, variableHeader));
                return;
            }
        } else {
            /**
             * @see MqttQoS#AT_MOST_ONCE 直接交给业务层处理
             * @see com.dream.iot.client.handle.ClientServiceHandler
             * @see MqttClientMessage#MqttClientMessage(MqttPublishMessage)
             */
            ctx.fireChannelRead(this.buildPublishMessage(ctx, msg));
            return;
        }

        try {
            /**
             * @see MqttClientMessage#MqttClientMessage(MqttPublishMessage)
             */
            final MqttClientMessage message = this.buildPublishMessage(ctx, msg);

            // 保存当前报文
            final MessageMapper mapper = new MessageMapper((MqttConnectProperties) properties, message, packetId);
            getMessageIdManager().addServer(packetId, mapper);

            // 服务端发布的Qos = 1 客户端返回ACK确认
            if(fixedHeader.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
                final MqttFixedHeader ackFixedHeader = new MqttFixedHeader(MqttMessageType.PUBACK
                        , false, MqttQoS.AT_MOST_ONCE, false, 0);

                final MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(packetId);
                ctx.channel().writeAndFlush(new MqttMessage(ackFixedHeader, variableHeader)).addListener(future -> {
                    // 如果ACK确认报文发送成功则移除对应的报文
                    if(future.isSuccess()) {
                        getMessageIdManager().removeServer(packetId);
                        if(logger.isTraceEnabled()) {
                            logger.trace("mqtt({}) {}(CLIENT) - PacketId：{} - 远程主机：{}"
                                    , getName(), MqttMessageType.PUBACK, packetId, remoteKey());
                        }
                    } else {
                        logger.warn("mqtt({}) {}(CLIENT) - PacketId：{} - 远程主机：{}"
                                , getName(), MqttMessageType.PUBACK, packetId, remoteKey(), future.cause());
                    }
                });

                ctx.fireChannelRead(message);

                // 服务端发布的Qos = 2 客户端返回Rec确认
            } else if(fixedHeader.qosLevel() == MqttQoS.EXACTLY_ONCE) {
                final MqttFixedHeader recFixedHeader = new MqttFixedHeader(MqttMessageType.PUBREC
                        , false, MqttQoS.AT_MOST_ONCE, false, 0);
                final MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(packetId);

                /**
                 * REC确认之后等待服务端返回REL确认
                 * @see #pubRel(MqttMessage, ChannelHandlerContext)
                 */
                ctx.channel().writeAndFlush(new MqttMessage(recFixedHeader, variableHeader)).addListener(future -> {
                    if(future.isSuccess()) {
                        if(logger.isTraceEnabled()) {
                            logger.trace("mqtt({}) {}(CLIENT) - PacketId：{} - 远程主机：{}"
                                    , getName(), MqttMessageType.PUBREC, packetId, remoteKey());
                        }
                    } else {
                        logger.warn("mqtt({}) {}(CLIENT) - PacketId：{} - 远程主机：{}"
                                , getName(), MqttMessageType.PUBREC, packetId, remoteKey(), future.cause());
                    }
                });
            }
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodException("没有匹配的构造函数["+getClientComponent().getMessageClass().getSimpleName()+"(MqttMessage)]");
        }
    }

    /**
     * 构建服务端发布的报文
     *
     * @param ctx
     * @param message
     * @return
     * @throws NoSuchMethodException
     */
    protected MqttClientMessage buildPublishMessage(ChannelHandlerContext ctx, MqttPublishMessage message) throws Exception {
        SocketMessage proxy = getClientComponent().proxy(ctx, message.content());
        if(proxy instanceof MqttClientMessage) {
            return ((MqttClientMessage) proxy).setMqttMessage(message).readBuild();
        } else {
            throw new MqttClientException("mqtt报文类型必须是["+MqttClientMessage.class.getSimpleName()+"]");
        }
    }

    /**
     * 服务端给客户端发送的PUBLISH确认报文(QoS1)
     * @see io.netty.handler.codec.mqtt.MqttQoS#AT_LEAST_ONCE
     * @param msg
     * @param ctx
     */
    protected void pubAck(MqttPubAckMessage msg, ChannelHandlerContext ctx) {
        final int messageId = msg.variableHeader().messageId();
        final MessageMapper remove = getMessageIdManager().remove(messageId);
        if(remove != null) {
            getClientComponent().getPublishListener().success(this, remove);
        }
    }

    /**
     * 服务端向客户端第一次确认(QoS2)
     * @see io.netty.handler.codec.mqtt.MqttQoS#EXACTLY_ONCE
     * @param msg
     * @param ctx
     */
    protected void pubRec(MqttMessage msg, ChannelHandlerContext ctx) {
        final MqttMessageIdVariableHeader idVariableHeader = (MqttMessageIdVariableHeader) msg.variableHeader();
        final int packetId = idVariableHeader.messageId();
        if(logger.isTraceEnabled()) {
            logger.trace("mqtt({}) {}(SERVER) - PacketId：{} - 远程主机：{}"
                    , getName(), MqttMessageType.PUBREC, packetId, remoteKey());
        }

        // 向服务端发送Rel报文, 告诉服务端可以删除对应的MsgId报文
        final MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBREL
                , false, MqttQoS.AT_MOST_ONCE, false, 0);

        ctx.channel().writeAndFlush(new MqttMessage(fixedHeader, msg.variableHeader())).addListener(future -> {
            if(future.isSuccess()) {
                if(logger.isTraceEnabled()) {
                    logger.trace("mqtt({}) {}(CLIENT) - PacketId：{} - 远程主机：{}"
                            , getName(), MqttMessageType.PUBREL, packetId, remoteKey());
                }
            } else {
                logger.warn("mqtt({}) {}(CLIENT) - PacketId：{} - 远程主机：{}"
                        , getName(), MqttMessageType.PUBREL, packetId, remoteKey(), future.cause());
            }
        });
    }

    /**
     * 服务端向客户端第二次确认(QoS2) 让客户端删除PacketId对应的Msg
     * @see io.netty.handler.codec.mqtt.MqttQoS#EXACTLY_ONCE
     * @param msg
     * @param ctx
     */
    protected void pubRel(MqttMessage msg, ChannelHandlerContext ctx) {
        final MqttMessageIdVariableHeader idVariableHeader = (MqttMessageIdVariableHeader) msg.variableHeader();
        final int packetId = idVariableHeader.messageId();
        final MqttFixedHeader header = new MqttFixedHeader(MqttMessageType.PUBCOMP, false, MqttQoS.AT_MOST_ONCE, false, 0);
        if(logger.isTraceEnabled()) {
            logger.trace("mqtt({}) {}(SERVER) - PacketId：{} - 远程主机：{}"
                    , getName(), MqttMessageType.PUBREL, packetId, remoteKey());
        }

        getChannel().writeAndFlush(new MqttMessage(header, idVariableHeader)).addListener(future -> {

            // 发送PUBCOMP成功, 删除PacketId对应的包
            if(future.isSuccess()) {
                final MessageMapper mapper = getMessageIdManager().removeServer(packetId);
                if(logger.isTraceEnabled()) {
                    logger.trace("mqtt({}) {}(CLIENT) - PacketId：{} - 远程主机：{}"
                            , getName(), MqttMessageType.PUBCOMP, packetId, remoteKey());
                }

                // 将报文上报给业务层处理器处理
                if(mapper != null) {
                    ctx.fireChannelRead(mapper.getMessage());
                }
            } else {
                logger.warn("mqtt({}) {}(CLIENT) - PacketId：{} - 远程主机：{}"
                        , getName(), MqttMessageType.PUBCOMP, packetId, remoteKey(), future.cause());
            }
        });
    }

    /**
     * 服务端向客户端发布第二次确认(QoS2)
     * 服务端通知客户端可以删除PacketId对应的报文了
     * @see io.netty.handler.codec.mqtt.MqttQoS#EXACTLY_ONCE
     * @param msg
     * @param ctx
     */
    protected void pubComp(MqttMessage msg, ChannelHandlerContext ctx) {
        final MqttMessageIdVariableHeader variableHeader = (MqttMessageIdVariableHeader)msg.variableHeader();
        final int messageId = variableHeader.messageId();
        if(logger.isTraceEnabled()) {
            logger.trace("mqtt({}) {}(SERVER) - PacketId：{} - 远程主机：{}"
                    , getName(), MqttMessageType.PUBCOMP, messageId, remoteKey());
        }

        // 服务端通知客户端可以删除MsgId对应的报文了
        final MessageMapper remove = getMessageIdManager().remove(messageId);
        if(remove != null) {
            getClientComponent().getPublishListener().success(this, remove);
        }
    }

    /**
     * 服务端响应的订阅确认报文
     * 客户端向服务器发送SUBSCRIBE报文用于创建一个或多个订阅。
     * 在服务器中，会记录这个客户关注的一个或者多个主题，当服务器收到这些主题的PUBLISH报文的时候，将分发应用消息到与之匹配的客户端中。
     * SUBSCRIBE报文支持通配符，也为每个订阅指定了最大的QoS等级，服务器根据这些信息分发应用消息给客户端。
     * SUBSCRIBE报文拥有固定报头、可变报头、有效载荷。
     * 当服务器收到客户端发送的一个SUBSCRIBE报文时，必须向客户端发送一个SUBACK报文响应，同时SUBACK报文必须和等待确认的SUBSCRIBE报文有相同的报文标识符。
     * 如果服务器收到一个SUBSCRIBE报文，报文的主题过滤器与一个现存订阅的主题过滤器相同，那么必须使用新的订阅彻底替换现存的订阅。新订阅的主题过滤器和之前订阅的相同，但是它的最大QoS值可以不同。与这个主题过滤器匹配的任何现存的保留消息必须被重发，但是发布流程不能中断。
     * @param msg
     * @param ctx
     */
    protected void subAck(MqttSubAckMessage msg, ChannelHandlerContext ctx) {
        int i = msg.variableHeader().messageId();
        DecoderResult decoderResult = msg.decoderResult();
        if(decoderResult.isSuccess()) {
            if(logger.isTraceEnabled()) {
                ConnectProperties properties = ctx.channel().attr(CoreConst.CLIENT_KEY).get();
                logger.trace("mqtt({}) {}(成功) - PacketId: {} - 远程主机：{}"
                        , getName(), MqttMessageType.SUBACK, i, properties);
            }
        } else {
            ConnectProperties properties = ctx.channel().attr(CoreConst.CLIENT_KEY).get();
            logger.warn("mqtt({}) {}(未完成) - PacketId: {} - 远程主机：{}"
                    , getName(), MqttMessageType.SUBACK, i, properties, decoderResult.cause());
        }
    }

    /**
     *  取消订阅确认报文
     * @param msg
     * @param ctx
     */
    protected void unSubAck(MqttUnsubAckMessage msg, ChannelHandlerContext ctx) {
        DecoderResult result = msg.decoderResult();
        final int i = msg.variableHeader().messageId();
        MessageMapper mapper = this.getMessageIdManager().remove(Integer.valueOf(i));
        if(mapper != null) {
            if(result.isSuccess()) {
                if(logger.isTraceEnabled()) {
                    ConnectProperties properties = ctx.channel().attr(CoreConst.CLIENT_KEY).get();
                    logger.trace("mqtt({}) {}(成功) - PacketId: {} - 远程主机：{} - msg：{}"
                            , getName(), MqttMessageType.UNSUBACK, i, properties, msg);
                }
            } else {
                logger.warn("mqtt({}) {}(未完成) - PacketId: {} - 远程主机：{} - msg：{}"
                        , getName(), MqttMessageType.UNSUBACK, i, remoteKey(), msg, result.cause());
            }
        }
    }

    /**
     * 客户端ping响应报文
     * @param msg
     * @param ctx
     */
    protected void pingResp(MqttMessage msg, ChannelHandlerContext ctx) {
        if(logger.isTraceEnabled()) {
            logger.trace("mqtt({}) {} - 远程主机: {}", getName(), MqttMessageType.PINGRESP, remoteKey());
        }
    }

    protected void doWriteAndFlush(MqttMessageType messageType, Channel channel, MqttMessage message, Integer messageId) {
        channel.writeAndFlush(message).addListener(future -> {
            if(future.isSuccess()) {

                if(messageId != null) {
                    if(logger.isTraceEnabled()) {
                        logger.trace("mqtt({}) {} - PacketId: {} - 远程主机：{}"
                                , getName(), messageType, messageId, remoteKey());
                    }

                } else {
                    if(logger.isTraceEnabled()) {
                        logger.trace("mqtt({}) {} - 远程主机：{}"
                                , getName(),  messageType, remoteKey());
                    }
                }
            } else if(future.cause() != null) {
                logger.error("mqtt({}) {} - 远程主机：{} - 报文：{}"
                        , getName(), messageType, remoteKey(), message, future.cause());
            }
        });
    }

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.disconnect(promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.close(promise);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(msg instanceof ClientSocketProtocol) {
            ClientSocketProtocol clientProtocol = (ClientSocketProtocol) msg;
            ClientMessage clientMessage = clientProtocol.requestMessage();
            if(msg instanceof ProtocolPreservable) {
                if(((ProtocolPreservable) msg).isRelation()) {
                    getClientComponent().protocolFactory().add((String) ((ProtocolPreservable) msg)
                            .relationKey(), (Protocol) msg, ((ProtocolPreservable) msg).getTimeout());
                }
            }

            // 如果是服务端主动的请求, 使用响应报文
            if(msg instanceof ServerInitiativeProtocol) {
                clientMessage = clientProtocol.responseMessage();
            }

            try {
                /**
                 * 只适用于发布类型
                 * @see MqttMessageType#PUBLISH
                 */
                if(clientMessage instanceof MqttClientMessage) {
                    final MqttClientMessage mqttClientMessage = (MqttClientMessage) clientMessage;
                    MqttPublishMessage mqttMessage = buildPublishMqttMessage(mqttClientMessage);
                    if(logger.isTraceEnabled()) {
                        final int packetId = mqttMessage.variableHeader().packetId();
                        logger.trace("mqtt({}) PUBLISH(CLIENT {}) - PacketId：{} - 报文：{}"
                                , getName(), mqttClientMessage.getQos(), packetId
                                , debugMessage(clientMessage, mqttMessage));
                    }

                    ctx.write(mqttMessage, promise).addListener(future -> {
                        final MqttFixedHeader header = mqttMessage.fixedHeader();
                        /**
                         * {@link MqttQoS#AT_LEAST_ONCE} 和 {@link MqttQoS#EXACTLY_ONCE} 需要等待Ack处理
                         *
                         * {@link MqttQoS#AT_LEAST_ONCE}
                         * @see MqttClientComponent#pubAck(MqttPubAckMessage, ChannelHandlerContext, List)
                         *
                         * {@link MqttQoS#EXACTLY_ONCE}
                         * @see MqttClientComponent#pubRec(MqttMessage, ChannelHandlerContext, List)
                         * @see MqttClientComponent#pubRel(MqttMessage, ChannelHandlerContext, List)
                         * @see MqttClientComponent#pubComp(MqttMessage, ChannelHandlerContext, List)
                         */
                        if(header.qosLevel() != MqttQoS.AT_MOST_ONCE) {
                            final int packetId = mqttMessage.variableHeader().packetId();
                            final MessageMapper mapper = new MessageMapper(getConfig(), mqttClientMessage, packetId);
                            getMessageIdManager().add(packetId, mapper);
                        }
                    });
                } else {
                    throw new ProtocolException("mqtt客户端只支持报文类型[MqttClientMessage]");
                }
            } catch (Exception e) {
                throw new ProtocolException("发布报文失败", e);
            }
        } else {
            ctx.write(msg, promise);
        }
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
