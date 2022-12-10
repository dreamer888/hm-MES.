package com.dream.iot.client.codec;

import com.dream.iot.message.UnParseBodyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public abstract class DatagramPacketToMessageDecoder extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        final ByteBuf content = msg.content();
        final int readableBytes = content.readableBytes();
        if(readableBytes > 0) {
            msg.retain();

            final byte[] bytes = new byte[readableBytes];
            content.readBytes(bytes);

            ctx.fireChannelRead(channelReadMessage(bytes).readBuild());
        }
    }

    protected abstract UnParseBodyMessage channelReadMessage(byte[] message);
}
