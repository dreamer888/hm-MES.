package com.dream.iot.server.udp.impl;

import com.dream.iot.server.udp.UdpServerMessage;
import com.dream.iot.udp.UdpMessageBody;
import com.dream.iot.udp.UdpMessageHead;

import java.net.InetSocketAddress;

public class DefaultUdpServerMessage extends UdpServerMessage {

    protected DefaultUdpServerMessage(byte[] message) {
        super(message);
    }

    public DefaultUdpServerMessage(byte[] message, InetSocketAddress recipient) {
        super(message, recipient);
    }

    public DefaultUdpServerMessage(UdpMessageHead head, InetSocketAddress recipient) {
        super(head, recipient);
    }

    public DefaultUdpServerMessage(UdpMessageHead head, UdpMessageBody body, InetSocketAddress recipient) {
        super(head, body, recipient);
    }

    public DefaultUdpServerMessage(UdpMessageHead head, InetSocketAddress sender, InetSocketAddress recipient) {
        super(head, sender, recipient);
    }

    public DefaultUdpServerMessage(UdpMessageHead head, UdpMessageBody body, InetSocketAddress sender, InetSocketAddress recipient) {
        super(head, body, sender, recipient);
    }

    @Override
    protected UdpMessageHead doBuild(byte[] message) {
        return new UdpMessageHead();
    }
}
