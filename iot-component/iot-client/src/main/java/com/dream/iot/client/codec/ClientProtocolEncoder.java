package com.dream.iot.client.codec;

import com.dream.iot.client.ClientProtocolException;
import com.dream.iot.client.protocol.ClientInitiativeProtocol;
import com.dream.iot.client.protocol.ClientSocketProtocol;
import com.dream.iot.client.protocol.ServerInitiativeProtocol;
import com.dream.iot.Message;
import com.dream.iot.ProtocolException;
import com.dream.iot.ProtocolPreservable;
import com.dream.iot.client.ClientComponent;
import com.dream.iot.client.ClientMessage;
import com.dream.iot.message.UnParseBodyMessage;
import com.dream.iot.utils.ByteUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ClientProtocolEncoder extends MessageToMessageEncoder<ClientSocketProtocol> {

    private ClientComponent clientComponent;
    private static Logger logger = LoggerFactory.getLogger(ClientProtocolEncoder.class);

    public ClientProtocolEncoder(ClientComponent clientComponent) {
        this.clientComponent = clientComponent;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ClientSocketProtocol protocol, List<Object> out) throws Exception {
        try {
            if(protocol instanceof ProtocolPreservable) {
                if(((ProtocolPreservable) protocol).isRelation()) {
                    clientComponent.protocolFactory().add((String) ((ProtocolPreservable) protocol)
                            .relationKey(), protocol, ((ProtocolPreservable) protocol).getTimeout());
                }
            }

            // Udp协议直接写出
            if(protocol.getPacket() != null) {
                out.add(((ClientInitiativeProtocol<?>) protocol).getPacket());
            } else { // Tcp报文
                // 构建实体报文到二进制报文
                if(protocol instanceof ClientInitiativeProtocol) {
                    UnParseBodyMessage message = doBuild(protocol.requestMessage(), "客户端请求平台");
                    out.add(Unpooled.wrappedBuffer(message.getMessage()));
                } else if(protocol instanceof ServerInitiativeProtocol) {
                    UnParseBodyMessage message = doBuild(protocol.responseMessage(), "客户端响应平台");
                    out.add(Unpooled.wrappedBuffer(message.getMessage()));
                } else {
                    logger.error("客户端报文编码({}) 不支持的协议类型", clientComponent.getName(), new ProtocolException("不支持的协议"));
                }
            }
        } catch (Exception e) {
            throw new ClientProtocolException("客户端协议编码异常：" + e.getMessage(), e, protocol);
        }
    }

    private UnParseBodyMessage doBuild(ClientMessage message, String desc) throws IOException {
        if(message.getMessage() == null) {
            message.writeBuild();
        }

        if(logger.isTraceEnabled()) {
            Message.MessageHead head = message.getHead();
            logger.trace("客户端报文编码({}) {} - 设备编号：{} - messageId: {}, 报文: {}"
                    ,clientComponent.getName(), desc, head.getEquipCode(), head.getMessageId(), ByteUtil.bytesToHexByFormat(message.getMessage()));
        }

        return message;
    }
}
