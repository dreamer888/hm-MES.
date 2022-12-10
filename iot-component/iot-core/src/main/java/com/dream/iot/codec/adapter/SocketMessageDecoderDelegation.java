package com.dream.iot.codec.adapter;

import com.dream.iot.SocketMessage;
import com.dream.iot.codec.SocketMessageDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCounted;

/**
 * 用来将{@link SocketMessageDecoder}解码操作委托给对应的组件{@link com.dream.iot.FrameworkComponent}
 * @see {@code SocketClientComponent} {@code SocketServerComponent}
 * @param <R>
 */
public interface SocketMessageDecoderDelegation<R extends ReferenceCounted> extends SocketMessageDecoder<R> {

    /**
     * 注意：一下两个方法不能被委托, 除非清楚原理否则不要将下面两个方法加入委托
     * @see #proxy(ChannelHandlerContext, ReferenceCounted)
     * @see #decodes(ChannelHandlerContext, ReferenceCounted)
     * @return
     */
    SocketMessageDecoder<R> getDelegation();

    SocketMessageDecoderDelegation setDelegation(SocketMessageDecoder<R> delegation);

    @Override
    default boolean isDecoder(Channel channel, ReferenceCounted msg) {
        return this.getDelegation().isDecoder(channel, msg);
    }

    @Override
    default SocketMessage doUdpDecode(ChannelHandlerContext ctx, DatagramPacket in) {
        return this.getDelegation().doUdpDecode(ctx, in);
    }

    @Override
    default SocketMessage doTcpDecode(ChannelHandlerContext ctx, ByteBuf in) {
        return this.getDelegation().doTcpDecode(ctx, in);
    }

    @Override
    default Class<? extends SocketMessage> getMessageClass() {
        return this.getDelegation().getMessageClass();
    }

    @Override
    default SocketMessage createMessage(byte[] message) {
        return this.getDelegation().createMessage(message);
    }
}
