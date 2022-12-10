package com.dream.iot.protocol;

import com.dream.iot.SocketMessage;
import com.dream.iot.AbstractProtocol;
import com.dream.iot.server.ServerMessage;

/**
 * 此协议将不做任何处理
 */
public class NoneDealProtocol extends AbstractProtocol<SocketMessage> {

    private static NoneDealProtocol instance;

    protected NoneDealProtocol(ServerMessage message) {
        this.requestMessage = message;
    }

    public static NoneDealProtocol getInstance(ServerMessage message) {
        if(instance != null) return instance;

        instance = new NoneDealProtocol(message);

        return instance;
    }


    @Override
    public CommonProtocolType protocolType() {
        return CommonProtocolType.NoneMap;
    }

    @Override
    public AbstractProtocol buildRequestMessage() {
        throw new UnsupportedOperationException("不支持此操作");
    }

    @Override
    public AbstractProtocol buildResponseMessage() {
        throw new UnsupportedOperationException("不支持此操作");
    }
}
