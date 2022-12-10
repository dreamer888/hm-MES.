package com.dream.iot.server;

import com.dream.iot.protocol.socket.AbstractSocketProtocol;

/**
 * 用来声明此协议是一个服务端且基于socket的协议
 * @param <M>
 */
public abstract class ServerSocketProtocol<M extends ServerMessage> extends AbstractSocketProtocol<M> {

    @Override
    public M requestMessage() {
        return super.requestMessage();
    }

    @Override
    public M responseMessage() {
        return super.responseMessage();
    }
}
