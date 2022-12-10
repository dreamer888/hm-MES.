package com.dream.iot.plc.omron;

import com.dream.iot.SocketMessage;
import com.dream.iot.client.IotClientBootstrap;
import com.dream.iot.client.component.TcpClientComponent;
import com.dream.iot.codec.adapter.LengthFieldBasedFrameMessageDecoderAdapter;
import com.dream.iot.event.ClientStatus;
import com.dream.iot.event.ClientStatusEvent;
import com.dream.iot.plc.PlcTcpClient;
import com.dream.iot.utils.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

import java.nio.ByteOrder;

/**
 * 欧姆龙PLC基于tcp实现的客户端
 */
public class OmronTcpClient extends PlcTcpClient {
    /*

在发送区输入：
46494E53 0000000C 00000000 00000000 00000003
点击发送，PLC立即回应：
46494E53 00000010 00000001 00000000 00000003 00000003

客户端ip地址03已被服务器03成功记录。


     */
    // 握手信号报文,,只能发一次， 重复发会返回错误码
    private final byte[] handSingleMessage = new byte[] {
        0x46, 0x49, 0x4E, 0x53, // FINS
        0x00, 0x00, 0x00, 0x0C, // 后面的命令长度
        0x00, 0x00, 0x00, 0x00, // 命令码
        0x00, 0x00, 0x00, 0x00, // 错误码
        0x00, 0x00, 0x00, 0x01  // 节点号
    };

    public OmronTcpClient(TcpClientComponent clientComponent, OmronConnectProperties config) {
        super(clientComponent, config);
        this.handSingleMessage[19] = config.getSA1();
    }

    @Override
    protected ChannelInboundHandler createProtocolDecoder() {
        return new LengthFieldBasedFrameMessageDecoderAdapter(ByteOrder.BIG_ENDIAN, 1024
                , 4, 4, 0, 0, true) {

            @Override
            public Class<? extends SocketMessage> getMessageClass() {
                return OmronMessage.class;
            }

            @Override
            public SocketMessage doTcpDecode(ChannelHandlerContext ctx, ByteBuf decode) {
                int readableBytes = (decode).readableBytes();
                byte[] message = new byte[readableBytes];
                (decode).readBytes(message);

                if(message.length != 24) {
                    String channelId = ctx.channel().id().asShortText();
                    return new OmronMessage(message).setChannelId(channelId);
                } else {
                    OmronTcpClient.this.setSuccess();

                    // 发布客户端上线事件
                    IotClientBootstrap.publishApplicationEvent(new ClientStatusEvent(this, ClientStatus.online, getClientComponent()));
                    if(logger.isDebugEnabled()) {
                        logger.debug("PLC({}) 握手响应 - 状态：{} - 报文：{}"
                                , getName(), "成功", ByteUtil.bytesToHex(message));
                    }
                }

                return null;
            }
        };
    }

    @Override
    public void successCallback(ChannelFuture future) {
        // 发送PLC的握手报文
        future.channel().writeAndFlush(Unpooled.wrappedBuffer(handSingleMessage)).addListener(call -> {
            if(call.isSuccess()) {
                if(logger.isDebugEnabled()) {
                    logger.debug("PLC({}) 握手请求 - 状态：{} - 报文：{}"
                            , getName(), "成功", ByteUtil.bytesToHex(handSingleMessage));
                }
            } else {
                logger.error("PLC({}) 握手请求 - 状态：{} - 报文：{}"
                        , getName(), "失败", ByteUtil.bytesToHex(handSingleMessage));
            }
        });
    }

    @Override
    public OmronConnectProperties getConfig() {
        return (OmronConnectProperties) super.getConfig();
    }
}
