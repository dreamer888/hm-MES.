package com.dream.iot.codec.adapter;

import com.dream.iot.SocketMessage;
import com.dream.iot.codec.SocketMessageDecoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

@ChannelHandler.Sharable
public class DatagramPacketDecoderAdapter extends SimpleChannelInboundHandler<DatagramPacket> implements SocketMessageDecoderDelegation<DatagramPacket> {

    private SocketMessageDecoder delegation;

    public DatagramPacketDecoderAdapter() {
        super(false);
    }

    @Override
    public SocketMessageDecoder<DatagramPacket> getDelegation() {
        return this.delegation;
    }

    @Override
    public SocketMessageDecoderDelegation setDelegation(SocketMessageDecoder<DatagramPacket> delegation) {
        this.delegation = delegation;
        return this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        final SocketMessage proxy = this.proxy(ctx, msg);
        if(proxy != null) {
            ctx.fireChannelRead(proxy);
        }
    }
}
