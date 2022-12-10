package com.dream.iot.message;

import com.dream.iot.SocketMessage;
import org.springframework.lang.NonNull;

/**
 * 未解析报文体
 */
public abstract class UnParseBodyMessage extends SocketMessage {

    public UnParseBodyMessage(byte[] message) {
        super(message);
    }

    public UnParseBodyMessage(@NonNull MessageHead head) {
        this(head, VOID_MESSAGE_BODY);
    }

    public UnParseBodyMessage(@NonNull MessageHead head, @NonNull MessageBody body) {
        super(head, body);
    }
}
