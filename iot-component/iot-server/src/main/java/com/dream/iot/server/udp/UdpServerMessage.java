package com.dream.iot.server.udp;

import com.dream.iot.server.ServerMessage;
import com.dream.iot.udp.UdpMessage;
import com.dream.iot.udp.UdpMessageBody;
import com.dream.iot.udp.UdpMessageHead;

import java.net.InetSocketAddress;

public abstract class UdpServerMessage extends ServerMessage implements UdpMessage {

    private InetSocketAddress sender;
    private InetSocketAddress recipient;

    public UdpServerMessage(byte[] message) {
        super(message);
    }

    public UdpServerMessage(byte[] message, InetSocketAddress recipient) {
        this(new UdpMessageHead(message), recipient);
    }

    public UdpServerMessage(UdpMessageHead head, InetSocketAddress recipient) {
        super(head);
        this.recipient = recipient;
    }

    public UdpServerMessage(UdpMessageHead head, UdpMessageBody body, InetSocketAddress recipient) {
        super(head, body);
        this.recipient = recipient;
    }

    public UdpServerMessage(UdpMessageHead head, InetSocketAddress sender, InetSocketAddress recipient) {
        super(head);
        this.sender = sender;
        this.recipient = recipient;
    }

    public UdpServerMessage(UdpMessageHead head, UdpMessageBody body, InetSocketAddress sender, InetSocketAddress recipient) {
        super(head, body);
        this.sender = sender;
        this.recipient = recipient;
    }

    @Override
    protected abstract UdpMessageHead doBuild(byte[] message);

    @Override
    public InetSocketAddress getSender() {
        return sender;
    }

    @Override
    public UdpServerMessage setSender(InetSocketAddress sender) {
        this.sender = sender;
        return this;
    }

    @Override
    public InetSocketAddress getRecipient() {
        return recipient;
    }

    @Override
    public UdpServerMessage setRecipient(InetSocketAddress recipient) {
        this.recipient = recipient;
        return this;
    }

    @Override
    public UdpMessageHead getHead() {
        return (UdpMessageHead) super.getHead();
    }

    @Override
    public UdpMessageBody getBody() {
        return (UdpMessageBody) super.getBody();
    }
}
