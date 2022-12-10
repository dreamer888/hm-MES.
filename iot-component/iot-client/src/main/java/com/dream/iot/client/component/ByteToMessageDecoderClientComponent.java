package com.dream.iot.client.component;

import com.dream.iot.SocketMessage;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.ClientMessage;
import com.dream.iot.client.MultiClientManager;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.codec.ByteToMessageDecoderClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class ByteToMessageDecoderClientComponent<M extends ClientMessage> extends TcpClientComponent<M>{

    public ByteToMessageDecoderClientComponent(ClientConnectProperties config) {
        super(config);
    }

    public ByteToMessageDecoderClientComponent(ClientConnectProperties config, MultiClientManager clientManager) {
        super(config, clientManager);
    }

    @Override
    public TcpSocketClient createNewClient(ClientConnectProperties config) {
        return new ByteToMessageDecoderClient(this, config);
    }

    @Override
    public abstract SocketMessage doTcpDecode(ChannelHandlerContext ctx, ByteBuf decode);
}
