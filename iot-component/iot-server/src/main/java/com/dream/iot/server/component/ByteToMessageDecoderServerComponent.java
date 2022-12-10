package com.dream.iot.server.component;

import com.dream.iot.codec.adapter.ByteToMessageDecoderAdapter;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.TcpServerComponent;
import com.dream.iot.server.ServerMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * create time: 2021/2/21
 *  适配{@link ByteToMessageDecoder}解码器到服务组件{@link TcpServerComponent}
 * @author dream
 * @since 1.0
 */
public abstract class ByteToMessageDecoderServerComponent<M extends ServerMessage> extends TcpDecoderServerComponent<M> {

    public ByteToMessageDecoderServerComponent(ConnectProperties connectProperties) {
        super(connectProperties);
    }

    @Override
    public ChannelInboundHandlerAdapter getMessageDecoder() {
        return new ByteToMessageDecoderAdapter(this);
    }

    /**
     * 自定义解码
     * @param ctx
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public abstract M doTcpDecode(ChannelHandlerContext ctx, ByteBuf in);
}
