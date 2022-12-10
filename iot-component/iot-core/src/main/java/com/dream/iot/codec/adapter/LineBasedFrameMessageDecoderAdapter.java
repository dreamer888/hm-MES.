package com.dream.iot.codec.adapter;

import com.dream.iot.SocketMessage;
import com.dream.iot.codec.SocketMessageDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LineBasedFrameDecoder;

public class LineBasedFrameMessageDecoderAdapter extends LineBasedFrameDecoder implements SocketMessageDecoderDelegation<ByteBuf> {

    private SocketMessageDecoder delegation;

    public LineBasedFrameMessageDecoderAdapter(int maxLength) {
        super(maxLength);
    }

    public LineBasedFrameMessageDecoderAdapter(int maxLength, boolean stripDelimiter, boolean failFast) {
        super(maxLength, stripDelimiter, failFast);
    }

    @Override
    public SocketMessage decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        return this.proxy(ctx, (ByteBuf) super.decode(ctx, in));
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
