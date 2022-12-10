package com.dream.iot.client.websocket;

import com.dream.iot.client.ClientMessage;
import com.dream.iot.websocket.WebSocketMessage;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class WebSocketClientMessage extends ClientMessage implements WebSocketMessage {

    private WebSocketClientConnectProperties properties;

    public WebSocketClientMessage(byte[] message) {
        super(message);
    }

    public WebSocketClientMessage(MessageHead head) {
        super(head);
    }

    public WebSocketClientMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }

    @Override
    public String uri() {
        return getProperties().getUri().getRawPath();
    }

    @Override
    public WebSocketVersion version() {
        return getProperties().getVersion();
    }

    @Override
    public Optional<String> getHeader(String key) {
        return Optional.ofNullable(getProperties()
                .getCustomHeaders()).map(item -> item.get(key));
    }

    @Override
    public Optional<String> getQueryParam(String key) {
        return Optional.ofNullable(getProperties().getUri().getRawQuery())
                .map(item -> Arrays.stream(item.split("&"))
                .map(param -> param.split("="))
                        .collect(Collectors.toMap(entry -> entry[0], entry-> entry[1])))
                .map(item -> item.get(key));
    }

    public WebSocketClientConnectProperties getProperties() {
        return properties;
    }

    public void setProperties(WebSocketClientConnectProperties properties) {
        this.properties = properties;
    }

}
