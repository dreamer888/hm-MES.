package com.dream.iot.client.component;

import com.dream.iot.SocketMessage;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.ClientMessage;
import com.dream.iot.client.MultiClientManager;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.codec.SimpleChannelInboundClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class SimpleChannelInboundClientComponent<M extends ClientMessage> extends TcpClientComponent<M>{

    public SimpleChannelInboundClientComponent(ClientConnectProperties config) {
        super(config);
    }

    public SimpleChannelInboundClientComponent(ClientConnectProperties config, MultiClientManager clientManager) {
        super(config, clientManager);
    }

    @Override
    public TcpSocketClient createNewClient(ClientConnectProperties config) {
        return new SimpleChannelInboundClient(this, config);
    }

    /**
     * @see ByteBuf#release()  需要自行释放
     * @param ctx
     * @param decode
     * @return
     */
    @Override
    public abstract SocketMessage doTcpDecode(ChannelHandlerContext ctx, ByteBuf decode);
}
