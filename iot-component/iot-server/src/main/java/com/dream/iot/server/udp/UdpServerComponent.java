package com.dream.iot.server.udp;

import com.dream.iot.Message;
import com.dream.iot.Protocol;
import com.dream.iot.SocketMessage;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.SocketServerComponent;
import com.dream.iot.udp.UdpProtocolException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.util.List;
import java.util.Optional;

public abstract class UdpServerComponent<M extends UdpServerMessage> extends SocketServerComponent<M, DatagramPacket> {

    /**
     * udp通道
     */
    private NioDatagramChannel channel;

    public UdpServerComponent(ConnectProperties connectProperties) {
        super(connectProperties);
    }

    @Override
    public SocketMessage proxy(ChannelHandlerContext ctx, DatagramPacket in) throws Exception {
        return super.proxy(ctx, in);
    }

    @Override
    public List<? extends SocketMessage> decodes(ChannelHandlerContext ctx, DatagramPacket in) throws Exception {
        return super.decodes(ctx, in);
    }

    @Override
    public Optional<ChannelFuture> writeAndFlush(String equipCode, Object msg, Object... args) {
        if(msg instanceof Protocol) {
            return this.writeAndFlush(equipCode, (Protocol) msg);
        } else if(msg instanceof Message){
            if(!(msg instanceof UdpServerMessage)) {
                throw new UdpProtocolException("udp协议只支持服务端报文类型["+UdpServerMessage.class.getSimpleName()+"]");
            }

            if(((UdpServerMessage) msg).getRecipient() == null) {
                throw new UdpProtocolException("未指定接收方地址[recipient]");
            }
        }

        return Optional.of(getChannel().writeAndFlush(msg));
    }

    @Override
    public Optional<ChannelFuture> writeAndFlush(String equipCode, Protocol protocol) {
        Message requestMessage = protocol.requestMessage();
        if(!(requestMessage instanceof UdpServerMessage)) {
            throw new UdpProtocolException("udp协议只支持服务端报文类型["+UdpServerMessage.class.getSimpleName()+"]");
        }

        if(((UdpServerMessage) requestMessage).getRecipient() == null) {
            throw new UdpProtocolException("未指定接收方地址[recipient]");
        }

        return Optional.of(getChannel().writeAndFlush(protocol));
    }

    public NioDatagramChannel getChannel() {
        return channel;
    }

    public void setChannel(NioDatagramChannel channel) {
        this.channel = channel;
    }
}
