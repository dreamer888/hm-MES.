package com.dream.iot.codec.adapter;

import com.dream.iot.SocketMessage;
import com.dream.iot.codec.SocketMessageDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class DelimiterBasedFrameMessageDecoderAdapter extends DelimiterBasedFrameDecoder implements SocketMessageDecoderDelegation<ByteBuf> {

    private SocketMessageDecoder delegation;

    public DelimiterBasedFrameMessageDecoderAdapter(int maxFrameLength, ByteBuf delimiter) {
        super(maxFrameLength, delimiter);
    }

    public DelimiterBasedFrameMessageDecoderAdapter(int maxFrameLength, boolean stripDelimiter, ByteBuf delimiter) {
        super(maxFrameLength, stripDelimiter, delimiter);
    }

    public DelimiterBasedFrameMessageDecoderAdapter(int maxFrameLength, boolean stripDelimiter, boolean failFast, ByteBuf delimiter) {
        super(maxFrameLength, stripDelimiter, failFast, delimiter);
    }

    public DelimiterBasedFrameMessageDecoderAdapter(int maxFrameLength, ByteBuf... delimiters) {
        super(maxFrameLength, delimiters);
    }

    public DelimiterBasedFrameMessageDecoderAdapter(int maxFrameLength, boolean stripDelimiter, ByteBuf... delimiters) {
        super(maxFrameLength, stripDelimiter, delimiters);
    }

    public DelimiterBasedFrameMessageDecoderAdapter(int maxFrameLength, boolean stripDelimiter, boolean failFast, ByteBuf... delimiters) {
        super(maxFrameLength, stripDelimiter, failFast, delimiters);
    }

    @Override
    public SocketMessage decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        return this.proxy(ctx, (ByteBuf) super.decode(ctx, buffer));
    }

    @Override
    public SocketMessageDecoder<ByteBuf> getDelegation() {
        return this.delegation;
    }

    @Override
    public DelimiterBasedFrameMessageDecoderAdapter setDelegation(SocketMessageDecoder<ByteBuf> delegation) {
        this.delegation = delegation;
        return this;
    }
}
