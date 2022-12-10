package com.dream.iot.client.codec;

import com.dream.iot.Message;
import com.dream.iot.SocketMessage;
import com.dream.iot.client.websocket.WebSocketClientComponent;
import com.dream.iot.client.websocket.WebSocketClientMessage;
import com.dream.iot.codec.SocketMessageDecoder;
import com.dream.iot.codec.adapter.SocketMessageDecoderDelegation;
import com.dream.iot.websocket.WebSocketConnectHead;
import com.dream.iot.websocket.WebSocketException;
import com.dream.iot.websocket.WebSocketFrameType;
import com.dream.iot.websocket.WebSocketMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketClientDecoder extends SimpleChannelInboundHandler<Object> implements SocketMessageDecoderDelegation<ByteBuf> {

    private WebSocketClient client;
    private SocketMessageDecoder delegation;
    private WebSocketClientHandshaker handshaker;
    protected static Logger logger = LoggerFactory.getLogger(WebSocketClientDecoder.class);

    public WebSocketClientDecoder(WebSocketClient client) {
        super(false);
        this.client = client;
    }

    @Override
    public SocketMessageDecoder<ByteBuf> getDelegation() {
        return this.delegation;
    }

    @Override
    public WebSocketClientDecoder setDelegation(SocketMessageDecoder<ByteBuf> delegation) {
        this.delegation = delegation;
        return this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpResponse) {
            if(((HttpResponse) msg).decoderResult() == DecoderResult.SUCCESS) {
                this.handshaker.processHandshake(ctx.channel(), (HttpResponse) msg).addListener(future -> {
                    if(future.isSuccess()) {
                        SocketMessage message = createMessage(Message.EMPTY);
                        message.setChannelId(ctx.channel().id().asShortText());
                        message.setHead(new WebSocketConnectHead(message.getChannelId()));
                        ((WebSocketClientMessage) message).setProperties(this.client.getConfig());
                        if(logger.isDebugEnabled()) {
                            logger.debug("报文解码({}) 握手成功 - uri：{}", getClient().getName()
                                    , getClient().getConfig().getUri().toString());
                        }
                        ctx.fireChannelRead(message);
                    }
                });
            }
        } else if(msg instanceof WebSocketFrame) {
            WebSocketFrame frame = (WebSocketFrame) msg;
            if(frame.isFinalFragment()) {
                SocketMessage socketMessage = this.proxy(ctx, frame.content());
                if(socketMessage instanceof WebSocketMessage) {
                    if(frame instanceof TextWebSocketFrame) {
                        ((WebSocketMessage) socketMessage).setFrameType(WebSocketFrameType.Text);
                    } else if(frame instanceof BinaryWebSocketFrame){
                        ((WebSocketMessage) socketMessage).setFrameType(WebSocketFrameType.Binary);
                    } else if(frame instanceof CloseWebSocketFrame) {
                        // 收到服务端的关闭请求则主动断开链接
                        this.getClient().disconnect(true).addListener(future -> {
                            if(future.isSuccess()) {
                                if(logger.isDebugEnabled()) {
                                    logger.debug("报文解码({}) 连接关闭成功 - uri：{}", getClient().getName()
                                            , getClient().getConfig().getUri().toString());
                                }
                            } else {
                                logger.error("报文解码({}) 连接关闭失败 - uri：{}", getClient().getName()
                                        , getClient().getConfig().getUri().toString(), future.cause());
                            }
                        });
                        ((WebSocketMessage) socketMessage).setFrameType(WebSocketFrameType.Close);
                    }

                    ((WebSocketClientMessage) socketMessage).setProperties(this.client.getConfig());
                    ctx.fireChannelRead(socketMessage);
                } else {
                    ctx.fireExceptionCaught(new WebSocketException("websocket只支持报文类型["+WebSocketMessage.class+"]"));
                }
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if(delegation instanceof WebSocketClientComponent) {
            this.handshaker = ((WebSocketClientComponent<?>) delegation).createClientHandShaker(this.client);
            this.handshaker.handshake(ctx.channel()).addListener(future -> {
                if(!future.isSuccess()) {
                    ctx.fireExceptionCaught(future.cause());
                } else {
                    if(logger.isDebugEnabled()) {
                        WebSocketClient client = this.getClient();
                        logger.debug("报文解码({}) 发起握手 - uri：{}", client.getName(), client.getConfig().getUri().toString());
                    }
                }
            });
        }

        super.channelActive(ctx);
    }

    public WebSocketClient getClient() {
        return client;
    }

    public WebSocketClientHandshaker getHandshaker() {
        return handshaker;
    }
}
