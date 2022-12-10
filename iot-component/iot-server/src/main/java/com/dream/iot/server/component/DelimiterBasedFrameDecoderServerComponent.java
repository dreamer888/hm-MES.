package com.dream.iot.server.component;

import com.dream.iot.codec.adapter.DelimiterBasedFrameMessageDecoderAdapter;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.TcpServerComponent;
import com.dream.iot.server.ServerMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.util.List;

/**
 * create time: 2021/2/22
 *  适配{@link DelimiterBasedFrameDecoder}解码器到服务组件{@link TcpServerComponent}
 * @author dream
 * @since 1.0
 */
public abstract class DelimiterBasedFrameDecoderServerComponent<M extends ServerMessage> extends TcpDecoderServerComponent<M> {

    private final ByteBuf[] delimiters;
    private final int maxFrameLength;
    private final boolean stripDelimiter;
    private final boolean failFast;

    public DelimiterBasedFrameDecoderServerComponent(ConnectProperties connectProperties, int maxFrameLength, ByteBuf delimiter) {
        this(connectProperties, maxFrameLength, true, delimiter);
    }

    public DelimiterBasedFrameDecoderServerComponent(ConnectProperties connectProperties, int maxFrameLength, boolean stripDelimiter, ByteBuf delimiter) {
        this(connectProperties, maxFrameLength, stripDelimiter, true, delimiter);
    }

    public DelimiterBasedFrameDecoderServerComponent(ConnectProperties connectProperties, int maxFrameLength, boolean stripDelimiter, boolean failFast, ByteBuf delimiter) {
        super(connectProperties);
        this.maxFrameLength = maxFrameLength;
        this.delimiters = new ByteBuf[]{delimiter};
        this.stripDelimiter = stripDelimiter;
        this.failFast = failFast;
    }

    @Override
    public ChannelInboundHandlerAdapter getMessageDecoder() {
        return new DelimiterBasedFrameMessageDecoderAdapter(maxFrameLength, stripDelimiter, failFast, this.delimiters).setDelegation(this);
    }

    @Override
    public List<M> decodes(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        throw new UnsupportedOperationException("不支持此方法, 请使用方法：DeviceMessageDecoder.decode(ctx, in)");
    }

}
