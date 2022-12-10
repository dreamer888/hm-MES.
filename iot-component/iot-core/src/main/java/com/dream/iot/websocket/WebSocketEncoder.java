package com.dream.iot.websocket;

import com.dream.iot.*;
import com.dream.iot.protocol.socket.AbstractSocketProtocol;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.ReferenceCounted;

import java.util.List;

@ChannelHandler.Sharable
public class WebSocketEncoder extends MessageToMessageEncoder<Object> {

    private FrameworkComponent component;

    public WebSocketEncoder(FrameworkComponent component) {
        this.component = component;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        SocketMessage writeMessage;
        Attribute<Boolean> closeFlag = ctx.channel().attr(CoreConst.WEBSOCKET_CLOSE);
        if(closeFlag.get() != null) {
            throw new WebSocketException("连接已关闭");
        }

        if(msg instanceof AbstractSocketProtocol) {
            writeMessage = ((AbstractSocketProtocol) msg).responseMessage();
            if(msg instanceof ProtocolPreservable) {
                if(((ProtocolPreservable) msg).isRelation()) {
                    component.protocolFactory().add((String) ((ProtocolPreservable) msg)
                            .relationKey(), (Protocol) msg, ((ProtocolPreservable) msg).getTimeout());
                }
            }
        } else if(msg instanceof WebSocketMessage) {
            writeMessage = (SocketMessage) msg;
        } else {
            // 处理编码器会自动释放一次
            if(msg instanceof ReferenceCounted) {
                ((ReferenceCounted) msg).retain();
            }
            out.add(msg);
            return;
        }

        if(writeMessage instanceof WebSocketMessage) {
            if(writeMessage.getMessage() == null) {
                writeMessage.writeBuild();
            }

            WebSocketFrameType type = ((WebSocketMessage) writeMessage).frameType();
            if(type == WebSocketFrameType.Text) {
                out.add(new TextWebSocketFrame(Unpooled.copiedBuffer(writeMessage.getMessage())));
            } else if(type == WebSocketFrameType.Binary) {
                out.add(new BinaryWebSocketFrame(Unpooled.copiedBuffer(writeMessage.getMessage())));
            } else if(type == WebSocketFrameType.Close) {
                out.add(createCloseWebSocketFrame(writeMessage));
                // 标识是关闭请求, 标识连接已经关闭
                closeFlag.set(Boolean.valueOf(true));
            } else {
                throw new WebSocketException("不支持的Websocket帧类型["+type+"]");
            }
        } else {
            throw new WebSocketException("Websocket只支持编码报文类型["+WebSocketMessage.class.getSimpleName()+"]");
        }
    }

    private CloseWebSocketFrame createCloseWebSocketFrame(SocketMessage requestMessage) {
        WebSocketCloseBody closeBody = (WebSocketCloseBody) requestMessage.getBody();
        return new CloseWebSocketFrame(closeBody.getStatus(), closeBody.getReasonText());
    }
}
