package com.dream.iot.server.component;

import com.dream.iot.SocketMessage;
import com.dream.iot.codec.adapter.LengthFieldBasedFrameMessageDecoderAdapter;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.ServerMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;
import java.util.List;

/**
 * create time: 2021/2/24
 *  基于长度字段的报文解码器组件
 * @see LengthFieldBasedFrameDecoder
 * @author dream
 * @since 1.0
 */
public abstract class LengthFieldBasedFrameDecoderServerComponent<M extends ServerMessage> extends TcpDecoderServerComponent<M> {

    private final ByteOrder byteOrder;
    private final int maxFrameLength;
    private final int lengthFieldOffset;
    private final int lengthFieldLength;
    private final int lengthAdjustment;
    private final int initialBytesToStrip;
    private final boolean failFast;

    public LengthFieldBasedFrameDecoderServerComponent(ConnectProperties connectProperties, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        this(connectProperties, maxFrameLength, lengthFieldOffset, lengthFieldLength, 0, 0);
    }

    public LengthFieldBasedFrameDecoderServerComponent(ConnectProperties connectProperties, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        this(connectProperties, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, true);
    }

    public LengthFieldBasedFrameDecoderServerComponent(ConnectProperties connectProperties, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        this(connectProperties, ByteOrder.BIG_ENDIAN, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }/**/

    public LengthFieldBasedFrameDecoderServerComponent(ConnectProperties connectProperties, ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(connectProperties);
        this.failFast = failFast;
        this.byteOrder = byteOrder;
        this.maxFrameLength = maxFrameLength;
        this.lengthAdjustment = lengthAdjustment;
        this.lengthFieldOffset = lengthFieldOffset;
        this.lengthFieldLength = lengthFieldLength;
        this.initialBytesToStrip = initialBytesToStrip;
    }

    @Override
    public ChannelInboundHandlerAdapter getMessageDecoder() {
        return new LengthFieldBasedFrameMessageDecoderAdapter(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast) {

            @Override
            public Class<? extends SocketMessage> getMessageClass() {
                return LengthFieldBasedFrameDecoderServerComponent.this.getMessageClass();
            }
        };
    }

    @Override
    public List<M> decodes(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        throw new UnsupportedOperationException("不支持此方法, 请使用方法：DeviceMessageDecoder.decode(ctx, in)");
    }

    public ByteOrder getByteOrder() {
        return byteOrder;
    }

    public int getMaxFrameLength() {
        return maxFrameLength;
    }

    public int getLengthFieldOffset() {
        return lengthFieldOffset;
    }

    public int getLengthFieldLength() {
        return lengthFieldLength;
    }

    public int getLengthAdjustment() {
        return lengthAdjustment;
    }

    public int getInitialBytesToStrip() {
        return initialBytesToStrip;
    }

    public boolean isFailFast() {
        return failFast;
    }
}
