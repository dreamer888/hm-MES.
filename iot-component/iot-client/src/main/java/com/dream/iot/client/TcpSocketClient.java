package com.dream.iot.client;

import com.dream.iot.client.component.TcpClientComponent;
import com.dream.iot.codec.SocketMessageDecoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * create time: 2021/8/6
 *
 * @author dream
 * @since 1.0
 */
public abstract class TcpSocketClient extends SocketClient {

    public TcpSocketClient(TcpClientComponent clientComponent, ClientConnectProperties config) {
        super(clientComponent, config);
    }

    protected Class<? extends Channel> channel() {
        return NioSocketChannel.class;
    }

    @Override
    public TcpClientComponent getClientComponent() {
        return (TcpClientComponent) super.getClientComponent();
    }

    /**
     * @see SocketMessageDecoder 基于netty的常用tcp解码器适配
     * @return
     */
    @Override
    protected abstract ChannelInboundHandler createProtocolDecoder();
}
