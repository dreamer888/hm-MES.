package com.dream.iot.client;

import com.dream.iot.AbstractProtocol;

/**
 * 用来声明此协议是客户端协议
 * @param <M>
 */
public abstract class AbstractClientProtocol<M extends ClientMessage> extends AbstractProtocol<M> implements ClientProtocol<M> {

    @Override
    public M requestMessage() {
        return super.requestMessage();
    }

    @Override
    public M responseMessage() {
        return super.responseMessage();
    }

    @Override
    public abstract AbstractClientProtocol buildRequestMessage();

    @Override
    public abstract AbstractClientProtocol buildResponseMessage();
}
