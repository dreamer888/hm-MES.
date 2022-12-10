package com.dream.iot.server.codec;

import com.dream.iot.CoreConst;
import com.dream.iot.ProtocolException;
import com.dream.iot.SocketMessage;
import com.dream.iot.codec.SocketMessageDecoder;
import com.dream.iot.codec.adapter.SocketMessageDecoderDelegation;
import com.dream.iot.server.websocket.WebSocketServerComponent;
import com.dream.iot.server.websocket.WebSocketServerComponentAbstract;
import com.dream.iot.websocket.WebSocketServerMessage;
import com.dream.iot.websocket.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCounted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;

@ChannelHandler.Sharable
public class WebSocketServerDecoder extends SimpleChannelInboundHandler<Object> implements SocketMessageDecoderDelegation<ReferenceCounted> {

    private SocketMessageDecoder delegation;
    private WebSocketServerComponentAbstract component;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public WebSocketServerDecoder(WebSocketServerComponentAbstract component) {
        super(false);
        this.component = component;
    }

    @Override
    public SocketMessageDecoder<ReferenceCounted> getDelegation() {
        return this.delegation;
    }

    @Override
    public SocketMessageDecoderDelegation setDelegation(SocketMessageDecoder<ReferenceCounted> delegation) {
        this.delegation = delegation;
        return this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof WebSocketFrame){
            //处理websocket客户端的消息
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        } else if(msg instanceof HttpRequest){
            //以http请求形式接入，但是走的是websocket
            handleHttpRequest(ctx, (HttpRequest) msg);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * @see #handlerWebSocketFrame(ChannelHandlerContext, WebSocketFrame) 在此构建报文
     * @param message
     * @return
     */
    @Override
    public SocketMessage readBuild(SocketMessage message) {
        return message; // 不直接构建报文
    }

    private ChannelFuture handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping消息处理
        if (frame instanceof PingWebSocketFrame) {
            return this.ping(ctx, frame);
        }

        if(frame.isFinalFragment()) {
            SocketMessage socketMessage = this.proxy(ctx, frame.content());
            if(socketMessage instanceof WebSocketServerMessage) {
                HttpRequestWrapper wrapper = ctx.channel().attr(CoreConst.WEBSOCKET_REQ).get();
                ((WebSocketServerMessage) socketMessage).setRequest(wrapper);
                if(frame instanceof TextWebSocketFrame) {
                    ((WebSocketServerMessage) socketMessage).setFrameType(WebSocketFrameType.Text);
                } else if(frame instanceof BinaryWebSocketFrame){
                    ((WebSocketServerMessage) socketMessage).setFrameType(WebSocketFrameType.Binary);
                } else if(frame instanceof CloseWebSocketFrame) {
                    ctx.close(); // 客户端主动关闭链接
                    if(logger.isDebugEnabled()) {
                        HttpRequestWrapper request = wrapper;
                        logger.debug("报文解码({}) 收到Close请求 - uri：{}", component.getName(), request.getUri());
                    }
                    ((WebSocketServerMessage) socketMessage).setFrameType(WebSocketFrameType.Close);
                }

                ctx.fireChannelRead(socketMessage.readBuild());
            }
        }

        return null;
    }

    protected ChannelFuture close(ChannelHandlerContext ctx, WebSocketFrame frame) {
        return ctx.channel().writeAndFlush(frame.retain()).addListener(ChannelFutureListener.CLOSE);
    }

    protected ChannelFuture ping(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if(logger.isDebugEnabled()) {
            HttpRequestWrapper request = ctx.channel().attr(CoreConst.WEBSOCKET_REQ).get();
            logger.debug("报文解码({}) 收到ping请求 - uri：{}", component.getName(), request.getUri());
        }
        return ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
    }

    /**
     * 处理升级为websocket前的唯一一次http请求
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req) {
        //要求Upgrade为websocket，过滤掉get/Post
        Optional<WebSocketFilter> filter = this.component.getFilter();
        if(!filter.orElse(WebSocketFilter.DEFAULT).handShaker(ctx.channel(), req)) {
            logger.warn("报文解码({}) Http Upgrade失败 - uri：{} - Upgrade：{}", component.getName()
                    , req.uri(), req.headers().get("Upgrade"), req.decoderResult().cause());
            return;
        }

        try {
            if(delegation instanceof WebSocketServerComponent) {
                WebSocketServerHandshaker handShaker = ((WebSocketServerComponent) delegation).createServerHandShaker(ctx, req);
                if (handShaker == null) {
                    WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
                } else {
                    handShaker.handshake(ctx.channel(), req).addListener(future -> {
                        if(future.isSuccess()) {
                            if(logger.isDebugEnabled()) {
                                logger.debug("报文解码({}) 握手成功 - uri：{}", component.getName(), req.uri());
                            }
                        } else {
                            logger.debug("报文解码({}) 握手失败 - uri：{}", component.getName(), req.uri(), future.cause());
                        }
                    });
                    ctx.channel().attr(CoreConst.WEBSOCKET_REQ).set(new HttpRequestWrapper(req, handShaker.version()));
                }
            } else {
                throw new WebSocketHandshakeException("创建WebSocketHandshake失败");
            }
        } catch (Exception e) {
            // 服务端异常
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    req.protocolVersion(), HttpResponseStatus.INTERNAL_SERVER_ERROR));

            throw new ProtocolException(e.getMessage(), e);
        }
    }

    /**
     * 拒绝不合法的请求，并返回错误信息
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx
            , HttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        // 如果是非Keep-Alive，关闭连接
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
