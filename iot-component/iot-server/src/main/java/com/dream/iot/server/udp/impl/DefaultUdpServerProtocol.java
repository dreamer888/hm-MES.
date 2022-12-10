package com.dream.iot.server.udp.impl;

import com.dream.iot.IotServeBootstrap;
import com.dream.iot.ProtocolType;
import com.dream.iot.server.SocketServerComponent;
import com.dream.iot.server.protocol.ClientInitiativeProtocol;
import com.dream.iot.udp.UdpMessageHead;
import com.dream.iot.udp.UdpProtocolException;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class DefaultUdpServerProtocol extends ClientInitiativeProtocol<DefaultUdpServerMessage> {

    private DefaultUdpServerMessage response;

    public DefaultUdpServerProtocol(DefaultUdpServerMessage requestMessage) {
        super(requestMessage);
    }

    /**
     * 响应内容到客户端
     * @param write
     */
    public void response(String write) {
        DefaultUdpServerMessage message = requestMessage();
        this.response = new DefaultUdpServerMessage(write.getBytes(StandardCharsets.UTF_8), message.getSender());
    }

    /**
     * 响应内容到客户端
     * @param message
     */
    public void response(byte[] message) {
        DefaultUdpServerMessage requestMessage = requestMessage();
        this.response = new DefaultUdpServerMessage(message, requestMessage.getSender());
    }

    /**
     * 以指定发送者响应到客户端
     * @param message
     * @param sender
     */
    public void response(byte[] message, InetSocketAddress sender) {
        DefaultUdpServerMessage requestMessage = requestMessage();
        this.response = new DefaultUdpServerMessage(new UdpMessageHead(message), sender, requestMessage.getRecipient());
    }

    /**
     * 响应到客户端
     * @param message 响应内容
     * @param recipient 自定义客户端地址
     * @param sender 发送方地址
     */
    public void response(byte[] message, InetSocketAddress recipient, InetSocketAddress sender) {
        this.response = new DefaultUdpServerMessage(new UdpMessageHead(message), sender, recipient);
    }

    /**
     * 写报文到指定客户端
     * @param message 要发送的内容
     * @param recipient 客户端地址
     * @return
     */
    public static ChannelFuture write(byte[] message, InetSocketAddress recipient) {
        SocketServerComponent serverComponent = IotServeBootstrap.getServerComponent(DefaultUdpServerMessage.class);
        if(serverComponent instanceof DefaultUdpServerComponent) {
            return ((DefaultUdpServerComponent) serverComponent)
                    .getChannel().writeAndFlush(new DefaultUdpServerMessage(message, recipient));
        } else {
            throw new UdpProtocolException("未注入组件到spring容器["+DefaultUdpServerComponent.class.getSimpleName()+"]");
        }
    }

    /**
     * 写报文到指定客户端
     * @param message 要发送的报文
     * @param recipient 接收方地址
     * @param sender 发送方地址
     * @return
     */
    public static ChannelFuture write(byte[] message, InetSocketAddress recipient, InetSocketAddress sender) {
        SocketServerComponent serverComponent = IotServeBootstrap.getServerComponent(DefaultUdpServerMessage.class);
        if(serverComponent instanceof DefaultUdpServerComponent) {
            return ((DefaultUdpServerComponent) serverComponent)
                    .getChannel().writeAndFlush(new DefaultUdpServerMessage(new UdpMessageHead(message), sender, recipient));
        } else {
            throw new UdpProtocolException("未注入组件到spring容器["+DefaultUdpServerComponent.class.getSimpleName()+"]");
        }
    }

    @Override
    protected DefaultUdpServerMessage doBuildResponseMessage() {
        return this.response;
    }

    @Override
    protected void doBuildRequestMessage(DefaultUdpServerMessage requestMessage) {

    }

    @Override
    public ProtocolType protocolType() {
        return DefaultUdpProtocolType.DEFAULT;
    }
}
