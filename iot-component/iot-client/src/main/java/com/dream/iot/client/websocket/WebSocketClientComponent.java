package com.dream.iot.client.websocket;

import com.dream.iot.client.codec.WebSocketClient;
import com.dream.iot.websocket.WebSocketComponent;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;

public interface WebSocketClientComponent<M extends WebSocketClientMessage> extends WebSocketComponent<M> {

    WebSocketClientHandshaker createClientHandShaker(WebSocketClient client);
}
