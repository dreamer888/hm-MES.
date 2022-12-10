package com.dream.iot.server.websocket;

import com.dream.iot.server.websocket.impl.DefaultWebSocketServerProtocol;

/**
 * 监听websocket服务端的帧事件处理器
 */
public interface WebSocketServerListener {

    /**
     * 要监听的uri 需要和请求uri做严格的匹配
     * @return
     */
    String uri();

    /**
     * 发送文本帧事件处理
     * @param protocol
     */
    default void onText(DefaultWebSocketServerProtocol protocol) {}

    /**
     * 发送关闭帧事件处理
     * @param protocol
     */
    default void onClose(DefaultWebSocketServerProtocol protocol) {}

    /**
     * 发送二进制事件处理
     * @param protocol
     */
    default void onBinary(DefaultWebSocketServerProtocol protocol) {}
}
