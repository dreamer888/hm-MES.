package com.dream.iot.test.server.udp;

import com.dream.iot.ProtocolType;
import com.dream.iot.server.protocol.ClientInitiativeProtocol;
import com.dream.iot.test.TestProtocolType;
import com.dream.iot.udp.UdpMessageHead;

import java.nio.charset.StandardCharsets;

public class UdpTestClientProtocol extends ClientInitiativeProtocol<UdpTestMessage> {

    public UdpTestClientProtocol(UdpTestMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected UdpTestMessage doBuildResponseMessage() {
        // 将客户端发送的报文原封返回
        // 客户端请求时sender是客户端地址, 服务端发送时sender就是接收方
        return new UdpTestMessage(new UdpMessageHead(requestMessage().getMessage()), requestMessage().getSender());
    }

    @Override
    protected void doBuildRequestMessage(UdpTestMessage requestMessage) {
        // 服务端主动发起请求给客户端
        // 客户端请求时sender是客户端地址, 服务端发送时sender就是接收方
        new UdpTestServerProtocol("请求".getBytes(StandardCharsets.UTF_8), requestMessage.getSender()).request();
    }

    @Override
    public ProtocolType protocolType() {
        return TestProtocolType.PIReq;
    }
}
