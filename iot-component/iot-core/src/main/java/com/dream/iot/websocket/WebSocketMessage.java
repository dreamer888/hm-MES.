package com.dream.iot.websocket;

import com.dream.iot.Message;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.util.Optional;

public interface WebSocketMessage extends Message {

    /**
     * 请求的uri
     * @return
     */
    String uri();

    /**
     * 使用的Websocket版本
     * @return
     */
    WebSocketVersion version();

    /**
     * 获取头参数
     * @param key
     * @return
     */
    Optional<String> getHeader(String key);

    /**
     * 请求的参数：/test?a=1
     * getQueryParam("a") = 1
     * @param key
     * @return
     */
    Optional<String> getQueryParam(String key);

    /**
     * WebSocket的帧类型
     * @see io.netty.handler.codec.http.websocketx.TextWebSocketFrame
     * @see io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame
     * @return
     */
    WebSocketFrameType frameType();

    WebSocketMessage setFrameType(WebSocketFrameType frameType);
}
