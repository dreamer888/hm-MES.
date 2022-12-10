package com.dream.iot.client.codec;

import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.component.TcpClientComponent;
import com.dream.iot.codec.adapter.DelimiterBasedFrameMessageDecoderAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInboundHandler;

public class DelimiterBasedFrameClient extends TcpSocketClient {

    private int maxFrameLength;
    private boolean stripDelimiter;
    private boolean failFast;
    private ByteBuf[] delimiter;

    public DelimiterBasedFrameClient(TcpClientComponent clientComponent, ClientConnectProperties config
            , int maxFrameLength, boolean stripDelimiter, boolean failFast, ByteBuf... delimiter) {
        super(clientComponent, config);
        this.failFast = failFast;
        this.delimiter = delimiter;
        this.maxFrameLength = maxFrameLength;
        this.stripDelimiter = stripDelimiter;
    }

    @Override
    protected ChannelInboundHandler createProtocolDecoder() {
        return new DelimiterBasedFrameMessageDecoderAdapter(this.getMaxFrameLength(), this.isStripDelimiter()
                , this.isFailFast(), this.getDelimiter());
    }

    public int getMaxFrameLength() {
        return maxFrameLength;
    }

    public boolean isStripDelimiter() {
        return stripDelimiter;
    }

    public boolean isFailFast() {
        return failFast;
    }

    public ByteBuf[] getDelimiter() {
        return delimiter;
    }
}
