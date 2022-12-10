package com.dream.iot.server.websocket;

import com.dream.iot.websocket.WebSocketComponent;
import com.dream.iot.websocket.WebSocketServerMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

public interface WebSocketServerComponent<M extends WebSocketServerMessage> extends WebSocketComponent<M> {

    WebSocketServerHandshaker createServerHandShaker(ChannelHandlerContext ctx, HttpRequest req);
}
