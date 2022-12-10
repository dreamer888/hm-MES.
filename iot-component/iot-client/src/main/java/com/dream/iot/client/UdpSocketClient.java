package com.dream.iot.client;

import com.dream.iot.client.component.UdpClientComponent;
import com.dream.iot.client.protocol.ClientSocketProtocol;
import com.dream.iot.client.codec.DatagramPacketToMessageDecoder;
import com.dream.iot.client.udp.UdpClientConnectProperties;
import com.dream.iot.client.udp.UdpClientMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.util.function.Consumer;

public abstract class UdpSocketClient extends SocketClient {

    public UdpSocketClient(UdpClientComponent clientComponent, UdpClientConnectProperties config) {
        super(clientComponent, config);
    }

    @Override
    protected void doInitOptions(Bootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_BROADCAST, true);
    }

    @Override
    protected Class<? extends Channel> channel() {
        return NioDatagramChannel.class;
    }

    @Override
    public ChannelFuture doConnect(Consumer<ChannelFuture> consumer, long timeout) {
        return getBootstrap().bind(0).addListener(future -> {
            connectLogger((ChannelFuture) future);
            consumer.accept((ChannelFuture) future);
        });
    }

    @Override
    protected abstract DatagramPacketToMessageDecoder createProtocolDecoder();

    /**
     * 写出的报文必须是 UdpRequestProtocol 协议类型
     * @param clientProtocol
     * @return
     */
    @Override
    public ChannelFuture writeAndFlush(ClientSocketProtocol clientProtocol) {
        // 写入Udp报文 DatagramPacket
        ClientMessage message = clientProtocol.requestMessage();
        if(message instanceof UdpClientMessage) {
            UdpClientConnectProperties config = getConfig();
            ((UdpClientMessage) message).setSender(config.getSender());
            ((UdpClientMessage) message).setRecipient(config.getRecipient());

            if(message.getMessage() == null) {
                message.writeBuild();
            }

            final ByteBuf byteBuf = Unpooled.wrappedBuffer(message.getMessage());
            if(config.getRecipient() != null && config.getSender() != null) {
                clientProtocol.setPacket(new DatagramPacket(byteBuf, config.getRecipient(), config.getSender()));
            } else {
                clientProtocol.setPacket(new DatagramPacket(byteBuf, config.getRecipient()));
            }
        } else {
            throw new IllegalArgumentException("Udp协议的客户端组件必须使用报文类型[UdpClientMessage]");
        }

        return super.writeAndFlush(clientProtocol);
    }

    @Override
    public UdpClientConnectProperties getConfig() {
        return (UdpClientConnectProperties) super.getConfig();
    }
}
