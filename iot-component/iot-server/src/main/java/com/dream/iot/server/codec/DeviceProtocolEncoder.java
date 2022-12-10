package com.dream.iot.server.codec;

import com.dream.iot.*;
import com.dream.iot.event.ExceptionEvent;
import com.dream.iot.server.TcpServerComponent;
import com.dream.iot.server.ServerComponentFactory;
import com.dream.iot.server.ServerSocketProtocol;
import com.dream.iot.server.protocol.ClientInitiativeProtocol;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Create Date By 2017-09-08
 *  平台协议编码器, 线程安全
 * @author dream
 * @since 1.7
 */
@ChannelHandler.Sharable
public class DeviceProtocolEncoder extends MessageToMessageEncoder<ServerSocketProtocol> {

    /**
     * 存储消息id和协议的映射关系
     * @see Protocol 协议
     */
    private ServerComponentFactory componentFactory;
    private Logger logger = LoggerFactory.getLogger(DeviceProtocolEncoder.class);

    public DeviceProtocolEncoder(ServerComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ServerSocketProtocol protocol, List<Object> out) throws Exception {

        try {
            //平台主动请求的则写出请求报文
            if(protocol instanceof ProtocolPreservable) {
                SocketMessage requestMessage = protocol.requestMessage();

                //如果需要保存映射关系,用relationKey作为关联映射
                if(((ProtocolPreservable) protocol).isRelation()){
                    final long timeout = ((ProtocolPreservable) protocol).getTimeout();

                    TcpServerComponent serverComponent = (TcpServerComponent) componentFactory.getByClass(requestMessage.getClass());
                    serverComponent.protocolFactory().add((String) ((ProtocolPreservable) protocol).relationKey(), protocol, timeout);
                }

                SocketMessage message = doBuild(requestMessage, "平台请求客户端");
                out.add(Unpooled.wrappedBuffer(message.getMessage()));

                //如果是设备请求的则写出响应报文
            } else if(protocol instanceof ClientInitiativeProtocol) {

                Message message = doBuild(protocol.responseMessage(), "平台响应客户端");
                out.add(Unpooled.wrappedBuffer(message.getMessage()));
            } else {
                logger.error("平台报文编码 不支持的协议类型", new ProtocolException("不支持的协议"));
            }
        } catch (Exception exception) {
            logger.error("平台报文编码 编码异常({}) - 设备编号：{} - 协议类型：{} - 已发送异常事件[ExceptionEvent]", exception.getCause()
                    , protocol.getEquipCode(), protocol.protocolType(), exception);
            IotServeBootstrap.publishApplicationEvent(new ExceptionEvent(exception, protocol.getEquipCode()));
        }

    }

    protected SocketMessage doBuild(SocketMessage message, String desc) throws IOException {
        if(message.getMessage() == null) {
            message.writeBuild();
        }

        if(logger.isTraceEnabled()) {
            Message.MessageHead head = message.getHead();
            logger.trace("平台报文编码 {} - 设备编号：{} - messageId: {}, 报文: {}"
                    , desc, head.getEquipCode(), head.getMessageId(), message);
        }
        return message;
    }
}
