package com.dream.iot.client.codec;

import com.dream.iot.SocketMessage;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.component.TcpClientComponent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.SimpleChannelInboundHandler;

public class SimpleChannelInboundClient extends TcpSocketClient {

    public SimpleChannelInboundClient(TcpClientComponent clientComponent, ClientConnectProperties config) {
        super(clientComponent, config);
    }

    /**
     * @see SimpleChannelInboundHandler
     * @return
     */
    @Override
    protected ChannelInboundHandler createProtocolDecoder() {
        // autoRelease 必须是 false
        return new SimpleChannelInboundHandler<ByteBuf>(false) {

            @Override
            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                SocketMessage socketMessage = SimpleChannelInboundClient.this.getClientComponent().proxy(ctx, msg);
                if(socketMessage != null) {
                    ctx.fireChannelRead(socketMessage);
                }
            }
        };
    }
}
