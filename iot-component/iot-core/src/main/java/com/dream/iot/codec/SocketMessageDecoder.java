package com.dream.iot.codec;

import com.dream.iot.ProtocolException;
import com.dream.iot.SocketMessage;
import com.dream.iot.codec.adapter.DelimiterBasedFrameMessageDecoderAdapter;
import com.dream.iot.codec.adapter.FixedLengthFrameDecoderAdapter;
import com.dream.iot.codec.adapter.LengthFieldBasedFrameMessageDecoderAdapter;
import com.dream.iot.codec.adapter.LineBasedFrameMessageDecoderAdapter;
import com.dream.iot.codec.filter.DecoderFilter;
import com.dream.iot.udp.UdpMessage;
import com.dream.iot.udp.UdpProtocolException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCounted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;

/**
 * IOT框架的解码器
 * 报文{@link SocketMessage}解码器
 * 使用此解码器务必保留默认的报文构造函数{@link SocketMessage#SocketMessage(byte[])}
 */
public interface SocketMessageDecoder<R extends ReferenceCounted> extends DecoderFilter {

    Logger DECODER_LOGGER = LoggerFactory.getLogger(SocketMessageDecoder.class);

    /**
     * 使用默认的构造函数 {@link SocketMessage#SocketMessage(byte[])} 构造实体报文
     * @param ctx
     * @param in 可以是类型 ByteBuf、SocketMessage、null
     * @return SocketMessage 使用默认的构造函数 {@link SocketMessage#SocketMessage(byte[])}
     * @throws Exception
     */
    default SocketMessage proxy(ChannelHandlerContext ctx, R in) throws Exception {
        // 先校验是否需要解码
        if(!isDecoder(ctx.channel(), in)) {
            return null;
        }

        SocketMessage message;
        if(in instanceof ByteBuf) {
            message = this.doTcpDecode(ctx, (ByteBuf) in);
        } else if(in instanceof DatagramPacket) {
            message = this.doUdpDecode(ctx, (DatagramPacket) in);
        } else {
            throw new IllegalStateException("解码失败, 不支持的类型["+in.getClass().getSimpleName()+"]");
        }

        if(message != null) {

            message.setRemote(ctx.channel().remoteAddress());
            // {@code SocketMessage#setChannelId(String)}必须在{@code AbstractMessage#readBuild()}之前调用
            message.setChannelId(ctx.channel().id().asShortText());

            // 打印解析出来的报文
            if(DECODER_LOGGER.isTraceEnabled()) {
                DECODER_LOGGER.trace("报文解码 解码成功 - 地址：{} - 类型：{} - 报文：{}", message.getRemote(), message.getClass().getSimpleName(), message);
            }

            // 构建报文
            return readBuild(message);
        }

        return null;
    }

    /**
     * 构建读取的报文
     * @param message
     * @return
     */
    default SocketMessage readBuild(SocketMessage message) {
        return message.readBuild();
    }

    /**
     * 解码报文列表
     * @param ctx
     * @param in
     * @return
     * @throws Exception
     */
    default List<? extends SocketMessage> decodes(ChannelHandlerContext ctx, R in) throws Exception {
        SocketMessage decode = proxy(ctx, in);
        if(decode != null) {
            return Arrays.asList(decode);
        } else {
            return null;
        }
    }

    /**
     * 需要自行释放报文 {@link ReferenceCounted#release()}
     * @param ctx
     * @param in
     * @return
     */
    default SocketMessage doUdpDecode(ChannelHandlerContext ctx, DatagramPacket in) {
        try {
            ByteBuf content = in.content();
            final int readableBytes = content.readableBytes();
            final byte[] message = new byte[readableBytes];
            content.readBytes(message);
            SocketMessage socketMessage = createMessage(message);
            if(socketMessage instanceof UdpMessage) {
                ((UdpMessage) socketMessage).setSender(in.sender());
                ((UdpMessage) socketMessage).setRecipient(in.recipient());
            } else {
                throw new UdpProtocolException("UDP协议必须使用[UdpMessage]");
            }

            return socketMessage;
        } finally {
            in.release();
        }
    }

    /**
     * 使用默认构造器创建报文 {@link SocketMessage#SocketMessage(byte[])}
     *
     * 获取所有可以读的报文, 默认不进行粘包处理<hr>
     *     粘包问题再各自的适配器进行处理
     * @see FixedLengthFrameDecoderAdapter#decode(ChannelHandlerContext, ByteBuf)
     * @see LineBasedFrameMessageDecoderAdapter#decode(ChannelHandlerContext, ByteBuf)
     * @see DelimiterBasedFrameMessageDecoderAdapter#decode(ChannelHandlerContext, ByteBuf)
     * @see LengthFieldBasedFrameMessageDecoderAdapter#decode(ChannelHandlerContext, ByteBuf)
     * @param ctx
     * @param in
     * @return
     */
    default SocketMessage doTcpDecode(ChannelHandlerContext ctx, ByteBuf in) {
        final int readableBytes = in.readableBytes();
        if(readableBytes > 0) {
            try {
                byte[] message = new byte[readableBytes];
                in.readBytes(message);
                return createMessage(message);
            } finally {
                in.release();
            }
        } else {
            return null;
        }
    }

    Class<? extends SocketMessage> getMessageClass();

    /**
     * 创建报文对象
     * @param message
     * @return
     */
    default SocketMessage createMessage(byte[] message) {
        // 报文必须保留默认的构造函数
        try {
            return BeanUtils.instantiateClass(getMessageClass().getConstructor(byte[].class), message);
        } catch (Exception e) {
            throw new ProtocolException("找不到构造函数["+getMessageClass().getSimpleName()+"(byte[])], 请增加对应的构造函数或者自定义解码", e.getCause());
        }
    }
}
