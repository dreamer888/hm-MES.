package com.dream.iot.server.codec;

import com.dream.iot.*;
import com.dream.iot.event.ExceptionEvent;
import com.dream.iot.server.ServerComponentFactory;
import com.dream.iot.server.ServerSocketProtocol;
import com.dream.iot.server.TcpServerComponent;
import com.dream.iot.server.protocol.ClientInitiativeProtocol;
import com.dream.iot.server.udp.UdpServerMessage;
import com.dream.iot.udp.UdpMessageHead;
import com.dream.iot.udp.UdpProtocolException;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@ChannelHandler.Sharable
public class UdpServerProtocolEncoder extends ChannelOutboundHandlerAdapter {

    /**
     * 存储消息id和协议的映射关系
     * @see Protocol 协议
     */
    private ServerComponentFactory componentFactory;
    private Logger logger = LoggerFactory.getLogger(DeviceProtocolEncoder.class);

    public UdpServerProtocolEncoder(ServerComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        DatagramPacket packet; UdpServerMessage message = null;

        try {
            if(msg instanceof ServerSocketProtocol) {
                ServerSocketProtocol protocol = (ServerSocketProtocol) msg;

                //平台主动请求的则写出请求报文
                if(protocol instanceof ProtocolPreservable) {
                    message = doBuild(protocol.requestMessage(), "平台请求客户端");
                    packet = new DatagramPacket(Unpooled.wrappedBuffer(message.getMessage()), message.getRecipient(), message.getSender());

                    //如果需要保存映射关系,用relationKey作为关联映射
                    if(((ProtocolPreservable) protocol).isRelation()){
                        final long timeout = ((ProtocolPreservable) protocol).getTimeout();

                        TcpServerComponent serverComponent = (TcpServerComponent) componentFactory.getByClass(message.getClass());
                        serverComponent.protocolFactory().add((String) ((ProtocolPreservable) protocol).relationKey(), protocol, timeout);
                    }

                    //如果是设备请求的则写出响应报文
                } else if(protocol instanceof ClientInitiativeProtocol) {
                    message = doBuild(protocol.responseMessage(), "平台响应客户端");
                    packet = new DatagramPacket(Unpooled.wrappedBuffer(message.getMessage()), message.getRecipient(), message.getSender());
                } else {
                    throw new UdpProtocolException("不支持的协议");
                }

                ctx.write(packet, promise);
            } else if(msg instanceof UdpServerMessage){
                message = doBuild((SocketMessage) msg, "平台请求客户端");
                packet = new DatagramPacket(Unpooled.wrappedBuffer(message.getMessage()), message.getRecipient(), message.getSender());
                ctx.write(packet, promise);
            } else {
                ctx.write(msg, promise);
            }
        } catch (Exception e) {
            if(message != null) {
                UdpMessageHead head = message.getHead();
                logger.error("UDP服务端编码 编码异常 - 接收方：{} - 协议类型：{} - 已发送异常事件[ExceptionEvent]"
                        ,message.getRecipient(), head != null ? head.getType() : "", e);
            } else {
                logger.error("UDP服务端编码 编码异常 - 已发送异常事件[ExceptionEvent]", e);
            }

            if(e instanceof ProtocolException) {
                ((ProtocolException) e).setProtocol(msg);
                IotServeBootstrap.publishApplicationEvent(new ExceptionEvent(e, null));
            } else {
                UdpProtocolException exception = new UdpProtocolException("编码失败", e, msg);
                IotServeBootstrap.publishApplicationEvent(new ExceptionEvent(exception, null));
            }
        }
    }

    protected UdpServerMessage doBuild(SocketMessage message, String desc) throws IOException {
        if(message.getMessage() == null) {
            message.writeBuild();
        }

        if(logger.isTraceEnabled()) {
            Message.MessageHead head = message.getHead();
            logger.trace("平台报文编码 {} - 设备编号：{} - messageId: {}, 报文: {}"
                    , desc, head.getEquipCode(), head.getMessageId(), message);
        }
        return (UdpServerMessage) message;
    }
}
