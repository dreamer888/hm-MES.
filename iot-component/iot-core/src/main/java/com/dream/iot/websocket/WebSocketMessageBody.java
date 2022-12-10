package com.dream.iot.websocket;

import com.dream.iot.message.DefaultMessageBody;

import java.nio.charset.StandardCharsets;

public class WebSocketMessageBody extends DefaultMessageBody {

    private String text;

    public WebSocketMessageBody(String text) {
        super(text.getBytes(StandardCharsets.UTF_8));
        this.text = text;
    }

    public WebSocketMessageBody(byte[] message) {
        super(message);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
