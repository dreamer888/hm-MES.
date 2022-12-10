package com.dream.iot.client.component;

import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.ClientMessage;
import com.dream.iot.client.MultiClientManager;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.codec.FixedLengthFrameClient;

/**
 * 使用固定长度解码器的客户端组件
 * @see FixedLengthFrameClient
 * @see io.netty.handler.codec.FixedLengthFrameDecoder
 * @param <M>
 */
public abstract class FixedLengthFrameClientComponent<M extends ClientMessage> extends TcpClientComponent<M> {

    private int frameLength;

    public FixedLengthFrameClientComponent(ClientConnectProperties config, int frameLength) {
        super(config);
        this.frameLength = frameLength;
    }

    public FixedLengthFrameClientComponent(ClientConnectProperties config, MultiClientManager clientManager, int frameLength) {
        super(config, clientManager);
        this.frameLength = frameLength;
    }

    @Override
    public TcpSocketClient createNewClient(ClientConnectProperties config) {
        return new FixedLengthFrameClient(this, config, this.frameLength);
    }

    public int getFrameLength() {
        return frameLength;
    }

    public void setFrameLength(int frameLength) {
        this.frameLength = frameLength;
    }
}
