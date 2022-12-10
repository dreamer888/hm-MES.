package com.dream.iot.codec.adapter;

import com.dream.iot.SocketMessage;
import com.dream.iot.codec.SocketMessageDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @see io.netty.channel.ChannelHandler.Sharable 如果是线程安全可以使用
 */
public class SimpleChannelDecoderAdapter extends SimpleChannelInboundHandler<ByteBuf> implements SocketMessageDecoderDelegation<ByteBuf> {

    private SocketMessageDecoder delegation;

    /**
     * autoRelease false
     * @param delegation
     */
    public SimpleChannelDecoderAdapter(SocketMessageDecoder delegation) {
        this(false, delegation);
    }

    /**
     * @param autoRelease
     * @param delegation
     */
    public SimpleChannelDecoderAdapter(boolean autoRelease, SocketMessageDecoder delegation) {
        super(autoRelease);
        this.delegation = delegation;
    }

    public SimpleChannelDecoderAdapter(Class<? extends ByteBuf> inboundMessageType, SocketMessageDecoder delegation) {
        this(inboundMessageType, false, delegation);
    }

    public SimpleChannelDecoderAdapter(Class<? extends ByteBuf> inboundMessageType, boolean autoRelease, SocketMessageDecoder delegation) {
        super(inboundMessageType, autoRelease);
        this.delegation = delegation;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        final SocketMessage proxy = this.proxy(ctx, msg);
        if(proxy != null) {
            ctx.fireChannelRead(proxy);
        }
    }

    @Override
    public SocketMessageDecoder<ByteBuf> getDelegation() {
        return this.delegation;
    }

    @Override
    public SocketMessageDecoderDelegation setDelegation(SocketMessageDecoder<ByteBuf> delegation) {
        this.delegation = delegation;
        return this;
    }
}
