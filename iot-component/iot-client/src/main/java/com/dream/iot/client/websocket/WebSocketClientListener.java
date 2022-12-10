package com.dream.iot.client.websocket;

import com.dream.iot.client.websocket.impl.DefaultWebSocketClientProtocol;

public interface WebSocketClientListener {

    /**
     * 要监听的uri 需要和请求uri做严格的匹配
     * @return
     */
    String uri();

    /**
     * 连接配置
     * @return
     */
    default WebSocketClientConnectProperties properties() {
        return new WebSocketClientConnectProperties(uri());
    }

    /**
     * 连接完成事件
     * @param protocol
     */
    void onConnect(DefaultWebSocketClientProtocol protocol);

    /**
     * 接收文本帧事件处理
     * @param protocol
     */
    default void onText(DefaultWebSocketClientProtocol protocol) {}

    /**
     * 接收关闭帧事件处理
     * @param protocol
     */
    default void onClose(DefaultWebSocketClientProtocol protocol) {}

    /**
     * 接收二进制事件处理
     * @param protocol
     */
    default void onBinary(DefaultWebSocketClientProtocol protocol) {}
}
