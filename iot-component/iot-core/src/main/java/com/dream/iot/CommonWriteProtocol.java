package com.dream.iot;

import com.dream.iot.business.BusinessFactory;
import com.dream.iot.server.ServerSocketProtocol;
import com.dream.iot.server.ServerMessage;

/**
 * 通用写协议
 */
public class CommonWriteProtocol extends ServerSocketProtocol<ServerMessage> {

    public CommonWriteProtocol(ServerMessage writeMessage) {
        this.requestMessage = this.responseMessage = writeMessage;
    }

    @Override
    public AbstractProtocol buildRequestMessage() {
        throw new UnsupportedOperationException("不支持操作");
    }

    @Override
    public AbstractProtocol buildResponseMessage() {
        throw new UnsupportedOperationException("不支持操作");
    }

    @Override
    public AbstractProtocol exec(BusinessFactory factory) {
        throw new UnsupportedOperationException("不支持操作");
    }

    @Override
    public AbstractProtocol exec(ProtocolHandle business) {
        throw new UnsupportedOperationException("不支持操作");
    }

    @Override
    public <T> T protocolType() {
        throw new UnsupportedOperationException("不支持操作");
    }
}
