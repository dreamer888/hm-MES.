package com.dream.iot.websocket;

public interface WebSocketServerMessage extends WebSocketMessage {

    HttpRequestWrapper request();

    WebSocketMessage setRequest(HttpRequestWrapper request);
}
