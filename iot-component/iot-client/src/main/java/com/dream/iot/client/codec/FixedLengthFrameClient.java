package com.dream.iot.client.codec;

import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.component.TcpClientComponent;
import com.dream.iot.codec.adapter.FixedLengthFrameDecoderAdapter;
import io.netty.channel.ChannelInboundHandler;

public class FixedLengthFrameClient extends TcpSocketClient {

    private int frameLength;

    public FixedLengthFrameClient(TcpClientComponent clientComponent, ClientConnectProperties config, int frameLength) {
        super(clientComponent, config);
        this.frameLength = frameLength;
    }

    @Override
    protected ChannelInboundHandler createProtocolDecoder() {
        return new FixedLengthFrameDecoderAdapter(frameLength);
    }

    public int getFrameLength() {
        return frameLength;
    }

    public void setFrameLength(int frameLength) {
        this.frameLength = frameLength;
    }
}
