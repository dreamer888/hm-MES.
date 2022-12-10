package com.dream.iot.client.udp;

import com.dream.iot.client.ClientMessage;
import com.dream.iot.udp.UdpMessage;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * 基于Udp协议的客户端报文
 */
public abstract class UdpClientMessage extends ClientMessage implements UdpMessage {

    private InetSocketAddress sender;
    private InetSocketAddress recipient;

    public UdpClientMessage(byte[] message) {
        super(message);
    }

    public UdpClientMessage(MessageHead head) {
        super(head);
    }

    public UdpClientMessage(MessageHead head, MessageBody body) {
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
