package com.dream.iot.server.dtu;

import com.dream.iot.CoreConst;
import com.dream.iot.ProtocolType;
import com.dream.iot.server.ServerMessage;
import com.dream.iot.server.dtu.message.DtuMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;

/**
 * dtu 上电第一包设备编号解码器<hr>
 *     1. 此handler要求dtu上报的第一包必须是设备编号
 *     2. 后面的报文如果内容和设备编号一致则作为心跳报文
 */
public class DtuFirstDeviceSnPackageHandler extends ChannelInboundHandlerAdapter {

    private DtuMessageDecoder messageDecoder;

    public DtuFirstDeviceSnPackageHandler(DtuMessageDecoder messageDecoder) {
        this.messageDecoder = messageDecoder;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            while (buf.isReadable()) { // 如果没有读完需等下一包的到来从而继续读取, 不做循环处理
                Attribute attr = ctx.channel().attr(CoreConst.EQUIP_CODE);
                if (attr.get() == null) { // 设备编号不存在说明是DTU第一次上报设备编号
                    buildDeviceSnMessage(ctx, buf);
                } else {
                    byte[] message = new byte[buf.readableBytes()];

                    // 解码异常 #I51ASQ
                    buf.duplicate().readBytes(message);
                    // 如果dtu除了上报设备编号的报文外, dtu自身还有其他的功能(心跳或者AT指令)
                    // 注：需要开发者自己做粘包处理
                    ServerMessage serverMessage = null;
                    try {
                        serverMessage = messageDecoder.decodeBefore((String) attr.get(), message, buf);
                    } catch (Exception e) {
                        // 如果异常直接放弃当前报文
                        buf.discardReadBytes();
                    }
                    if (serverMessage != null) {
                        ProtocolType protocolType = ((DtuMessage) serverMessage).getProtocolType();
                        // 交由下一个解码handle处理
                        if(protocolType == DtuCommonProtocolType.PASSED) {
                            super.channelRead(ctx, msg); // 此处的报文释放交由对应的解码器
                        } else { // 解码成功 交由下一个业务处理器处理
                            serverMessage.setChannelId(ctx.channel().id().asShortText());
                            super.channelRead(ctx, serverMessage.readBuild());

                            if(!buf.isReadable()) {
                                buf.release(); // 没有剩余的报文可读取 释放已经读取的报文
                            }
                        }
                    } else {
                        return; // 等待下一个报文的到来
                    }

                }
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }

    /**
     * 构建Dtu设备的第一个报文(上报设备编号)
     * @param ctx
     * @param buf
     * @throws Exception
     */
    private void buildDeviceSnMessage(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        try {
            byte[] message = new byte[buf.readableBytes()];
            buf.readBytes(message);
            String deviceSn = messageDecoder.resolveEquipCode(message);

            // 由于第一包是字符串类型的设备编号 不执行读构建{@link SocketMessage#readBuild()}
            ServerMessage dtuMessage = messageDecoder.createMessage(message);

            // 设置设备编号
            ((DtuMessage) dtuMessage).setEquipCode(deviceSn);

            // 设置DTU报文头
            dtuMessage.setHead(((DtuMessage) dtuMessage).buildFirstHead());

            // 交由下一个handler
            super.channelRead(ctx, dtuMessage);
        } finally {
            buf.release(); // 释放已经读取的二进制数据
        }
    }

}
