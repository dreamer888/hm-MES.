package com.dream.iot.client.codec;

import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.component.TcpClientComponent;
import com.dream.iot.codec.adapter.LengthFieldBasedFrameMessageDecoderAdapter;
import io.netty.channel.ChannelInboundHandler;

import java.nio.ByteOrder;

public class LengthFieldBasedFrameClient extends TcpSocketClient {

    private final ByteOrder byteOrder;
    private final int maxFrameLength;
    private final int lengthFieldOffset;
    private final int lengthFieldLength;
    private final int lengthAdjustment;
    private final int initialBytesToStrip;
    private final boolean failFast;

    public LengthFieldBasedFrameClient(TcpClientComponent clientComponent, ClientConnectProperties config
            , ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength
            , int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(clientComponent, config);
        this.failFast = failFast;
        this.byteOrder = byteOrder;

        this.maxFrameLength = maxFrameLength;
        this.lengthAdjustment = lengthAdjustment;
        this.lengthFieldOffset = lengthFieldOffset;
        this.lengthFieldLength = lengthFieldLength;

        this.initialBytesToStrip = initialBytesToStrip;
    }

    @Override
    protected ChannelInboundHandler createProtocolDecoder() {
        return new LengthFieldBasedFrameMessageDecoderAdapter(byteOrder, maxFrameLength
                , lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
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
