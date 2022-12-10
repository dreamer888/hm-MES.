package com.dream.iot.test.client.mutual;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.component.TcpClientComponent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 支持和模拟服务端交互的客户端
 */
public class MutualClientTestComponent extends TcpClientComponent<MutualClientMessage> {

    public MutualClientTestComponent(ClientConnectProperties config) {
        super(config);
    }

    @Override
    public TcpSocketClient createNewClient(ClientConnectProperties config) {
        return new TcpSocketClient(this, config) {
            @Override
            protected ChannelInboundHandler createProtocolDecoder() {
                return new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        int readableBytes = msg.readableBytes();
                        if(readableBytes > 0) {
                            byte[] message = new byte[readableBytes];
                            msg.readBytes(message);
                            ctx.fireChannelRead(new MutualClientMessage(message));
                        }
                    }
                };
            }
        };
    }

    @Override
    public String getName() {
        return "无格式解码";
    }

    @Override
    public String getDesc() {
        return "支持和模拟服务端交互的组件";
    }

    @Override
    public AbstractProtocol getProtocol(MutualClientMessage message) {
        return new MutualClientTestProtocol(message);
    }
}
