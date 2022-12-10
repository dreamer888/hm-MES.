package com.dream.iot.test.server.udp;

import com.dream.iot.ProtocolType;
import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.server.protocol.ServerInitiativeProtocol;
import com.dream.iot.test.TestProtocolType;

import java.io.IOException;
import java.net.InetSocketAddress;

public class UdpTestServerProtocol extends ServerInitiativeProtocol<UdpTestMessage> {

    private byte[] message;
    private InetSocketAddress recipient;

    public UdpTestServerProtocol(byte[] message) {
        this.message = message;
    }

    public UdpTestServerProtocol(byte[] message, InetSocketAddress recipient) {
        this.message = message;
        this.recipient = recipient;
    }

    @Override
    protected UdpTestMessage doBuildRequestMessage() throws IOException {
        DefaultMessageHead messageHead = new DefaultMessageHead(null, null, TestProtocolType.PIReq);
        messageHead.setMessage(this.message);
        return new UdpTestMessage(this.message, recipient);
    }

    @Override
    protected void doBuildResponseMessage(UdpTestMessage message) {

    }

    @Override
    public ProtocolType protocolType() {
        return TestProtocolType.PIReq;
    }
}
