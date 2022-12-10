package com.dream.iot.plc.siemens;

import com.dream.iot.SocketMessage;
import com.dream.iot.client.IotClientBootstrap;
import com.dream.iot.client.component.TcpClientComponent;
import com.dream.iot.codec.adapter.LengthFieldBasedFrameMessageDecoderAdapter;
import com.dream.iot.event.ClientStatus;
import com.dream.iot.event.ClientStatusEvent;
import com.dream.iot.plc.PlcException;
import com.dream.iot.plc.PlcTcpClient;
import com.dream.iot.utils.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

import java.nio.ByteOrder;

public class SiemensS7Client extends PlcTcpClient {

    private byte[] INIT_MESSAGE = new byte[] {
            0x03, 0x00, 0x00, 0x16, 0x11, (byte) 0xE0, 0x00, 0x00, 0x00, 0x01, 0x00, (byte) 0xC0, 0x01,
            0x0A, (byte) 0xC1, 0x02, 0x01, 0x02, (byte) 0xC2, 0x02, 0x01, 0x00
    };

    private byte[] SURE_MESSAGE = new byte[] {
            0x03, 0x00, 0x00, 0x19, 0x02, (byte) 0xF0, (byte) 0x80, 0x32, 0x01, 0x00, 0x00, 0x04, 0x00, 0x00, 0x08, 0x00, 0x00,
            (byte) 0xF0, 0x00, 0x00, 0x01, 0x00, 0x01, 0x01, (byte) 0xE0
    };

    public SiemensS7Client(TcpClientComponent clientComponent, SiemensConnectProperties config) {
        super(clientComponent, config);
    }

    @Override
    public SiemensConnectProperties getConfig() {
        return (SiemensConnectProperties) super.getConfig();
    }

    @Override
    protected ChannelInboundHandler createProtocolDecoder() {
        return new LengthFieldBasedFrameMessageDecoderAdapter(ByteOrder.BIG_ENDIAN
                , 1024, 2, 2, -4, 0, true) {

            @Override
            public Class<? extends SocketMessage> getMessageClass() {
                return SiemensS7Message.class;
            }

            @Override
            public SocketMessage doTcpDecode(ChannelHandlerContext ctx, ByteBuf decode) {
                int readableBytes = (decode).readableBytes();
                byte[] message = new byte[readableBytes];
                (decode).readBytes(message);
                if(message[8] == 0x03) { // 0x03 属于读写指令的响应
                    String messageId = ctx.channel().id().asShortText();
                    return new SiemensS7Message(message).setChannelId(messageId);
                }

                // 以下是协商报文
                if(readableBytes == 0x16) {
                    if(logger.isDebugEnabled()) {
                        logger.debug("PLC({}) 连接初始化响应 - 型号：{} - 状态：{} - 报文：{}"
                                , getName(), getConfig().getModel(), "成功", ByteUtil.bytesToHex(message));
                    }

                    ctx.channel().writeAndFlush(Unpooled.wrappedBuffer(SURE_MESSAGE)).addListener(call -> {
                        if(call.isSuccess()) {
                            if(logger.isDebugEnabled()) {
                                logger.debug("PLC({}) 报文协商请求 - 型号：{} - 状态：{} - 报文：{}"
                                        , getName(), getConfig().getModel(), "成功", ByteUtil.bytesToHex(SURE_MESSAGE));
                            }
                        } else {
                            SiemensS7Client.this.setFailure(new PlcException("报文协商第二阶段失败", call.cause()));
                            logger.error("PLC({}) 报文协商请求 - 型号：{} - 状态：{} - 报文：{}"
                                    , getName(), getConfig().getModel(), "失败", ByteUtil.bytesToHex(SURE_MESSAGE));
                        }
                    });
                } else {
                    SiemensS7Client.this.setSuccess();
                    // 发布客户端上线事件
                    IotClientBootstrap.publishApplicationEvent(new ClientStatusEvent(this, ClientStatus.online, getClientComponent()));

                    if(logger.isDebugEnabled()) {
                        logger.debug("PLC({}) 报文协商响应(初始化完成) - 型号：{} - 状态：{} - 报文：{}", getName(), getConfig().getModel(), "成功", ByteUtil.bytesToHex(message));
                    }
                }

                return null;
            }
        };
    }

    @Override
    public void successCallback(ChannelFuture future) {
        this.buildInitMessage();

        // 发送PLC的初始化报文
        future.channel().writeAndFlush(Unpooled.wrappedBuffer(INIT_MESSAGE)).addListener(call -> {
            if(call.isSuccess()) {
                if(logger.isDebugEnabled()) {
                    logger.debug("PLC({}) 连接初始化请求 - 型号：{} - 状态：{} - 报文：{}"
                            , getName(), getConfig().getModel(), "成功", ByteUtil.bytesToHex(INIT_MESSAGE));
                }
            } else {
                this.setFailure(new PlcException("报文协商第一阶段失败", call.cause()));
                logger.error("PLC({}) 连接初始化请求 - 型号：{} - 状态：{} - 报文：{}"
                        , getName(), getConfig().getModel(), "失败", ByteUtil.bytesToHex(INIT_MESSAGE));
            }
        });
    }

    private byte[] plcOrderNumber = new byte[] {
        0x03, 0x00, 0x00, 0x21, 0x02, (byte) 0xF0, (byte) 0x80, 0x32, 0x07, 0x00, 0x00, 0x00, 0x01, 0x00, 0x08, 0x00, 0x08,
        0x00, 0x01, 0x12, 0x04, 0x11, 0x44, 0x01, 0x00, (byte) 0xFF, 0x09, 0x00, 0x04, 0x00, 0x11, 0x00, 0x00
    };

    private byte[] INIT_S200smart = new byte[]  {
        0x03, 0x00, 0x00, 0x16, 0x11, (byte) 0xE0, 0x00, 0x00, 0x00, 0x01, 0x00, (byte) 0xC1, 0x02, 0x10, 0x00, (byte) 0xC2,
        0x02, 0x03, 0x00, (byte) 0xC0, 0x01, 0x0A
    };

    private byte[] SURE_S200smart = new byte[] {
        0x03, 0x00, 0x00, 0x19, 0x02, (byte) 0xF0, (byte) 0x80, 0x32, 0x01, 0x00, 0x00, (byte) 0xCC, (byte) 0xC1,
        0x00, 0x08, 0x00, 0x00, (byte) 0xF0, 0x00, 0x00, 0x01, 0x00, 0x01, 0x03, (byte) 0xC0
    };

    private byte[] INIT_S200 = new byte[] {
        0x03, 0x00, 0x00, 0x16, 0x11, (byte) 0xE0, 0x00, 0x00, 0x00, 0x01, 0x00, (byte) 0xC1, 0x02, 0x4D, 0x57, (byte) 0xC2,
        0x02, 0x4D, 0x57, (byte) 0xC0, 0x01, 0x09
    };

    private byte[] SURE_S200 = new byte[] {
        0x03, 0x00, 0x00, 0x19, 0x02, (byte) 0xF0, (byte) 0x80, 0x32, 0x01, 0x00, 0x00, 0x00, 0x00,
        0x00, 0x08, 0x00, 0x00, (byte) 0xF0, 0x00, 0x00, 0x01, 0x00, 0x01, 0x03, (byte) 0xC0
    };

    public void buildInitMessage() {
        SiemensModel model = getConfig().getModel();
        switch (model) {
            case S200:
                INIT_MESSAGE = INIT_S200;
                SURE_MESSAGE = SURE_S200;
                break;
            case S200Smart:
                INIT_MESSAGE = INIT_S200smart;
                SURE_MESSAGE = SURE_S200smart;
                break;
            case S300:
                INIT_MESSAGE[21] = 2;
                resetRackAndSlot();
                break;
            case S400:
                INIT_MESSAGE[17] = 0;
                INIT_MESSAGE[21] = 3;
                resetRackAndSlot();
                break;
            case S1200:
            case S1500:
                INIT_MESSAGE[21] = 0;
                resetRackAndSlot();
                break;
        }
    }

    protected void resetRackAndSlot() {
        if(this.getConfig().getRack() != 0 || this.getConfig().getSlot() != 0) {
            INIT_MESSAGE[21] = (byte) ((this.getConfig().getRack() * 0x20) + this.getConfig().getSlot());
        }
    }
}
