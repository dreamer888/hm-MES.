package com.dream.iot.server;

import com.dream.iot.message.UnParseBodyMessage;

/**
 * create time: 2021/8/8
 *  用于声明此报文是一个服务端报文
 * @author dream
 * @since 1.0
 */
public abstract class ServerMessage extends UnParseBodyMessage {

    /**
     * 此构造函数必须在子类里面
     * @param message
     */
    public ServerMessage(byte[] message) {
        super(message);
    }

    public ServerMessage(MessageHead head) {
        super(head);
    }

    public ServerMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }

    @Override
    public ServerMessage readBuild() {
        return (ServerMessage) super.readBuild();
    }
}
