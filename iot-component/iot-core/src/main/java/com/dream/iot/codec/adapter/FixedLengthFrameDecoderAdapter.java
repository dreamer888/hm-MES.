package com.dream.iot.codec.adapter;

import com.dream.iot.SocketMessage;
import com.dream.iot.codec.SocketMessageDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.FixedLengthFrameDecoder;

/**
 * create time: 2021/8/13
 *  固定字段解码适配器
 * @author dream
 * @since 1.0
 */
public class FixedLengthFrameDecoderAdapter extends FixedLengthFrameDecoder implements SocketMessageDecoderDelegation<ByteBuf> {

    private SocketMessageDecoder delegation;

    /**
     * Creates a new instance.
     *
     * @param frameLength the length of the frame
     */
    public FixedLengthFrameDecoderAdapter(int frameLength) {
        super(frameLength);
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
