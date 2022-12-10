package com.dream.iot.plc;

import com.dream.iot.client.ClientMessage;
import com.dream.iot.udp.UdpMessage;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public abstract class PlcUdpClientMessage extends PlcClientMessage implements UdpMessage  {
    private InetSocketAddress sender;
    private InetSocketAddress recipient;

    public PlcUdpClientMessage(byte[] message) {
        super(message);
    }

    public PlcUdpClientMessage(MessageHead head) {
        super(head);
    }

    public PlcUdpClientMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }

    @Override
    public InetSocketAddress getSender() {
        return this.sender;
    }

    @Override
    public UdpMessage setSender(InetSocketAddress sender) {
        this.sender = sender;
        return this;
    }

    @Override
    public SocketAddress getRemote() {
        return this.getRecipient();
    }

    @Override
    public void setRemote(SocketAddress remote) {
        this.setRecipient((InetSocketAddress) remote);
    }

    @Override
    public InetSocketAddress getRecipient() {
        return this.recipient;
    }

    @Override
    public UdpMessage setRecipient(InetSocketAddress recipient) {
        this.recipient = recipient;
        return this;
    }
}
