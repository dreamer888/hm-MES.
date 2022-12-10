package com.dream.iot.test.server.udp;

import com.dream.iot.server.udp.UdpServerMessage;
import com.dream.iot.udp.UdpMessageHead;

import java.net.InetSocketAddress;

/**
 * create time: 2021/9/12
 *
 * @author dream
 * @since 1.0
 */
public class UdpTestMessage extends UdpServerMessage {

    public UdpTestMessage(byte[] message) {
        super(message);
    }

    public UdpTestMessage(byte[] message, InetSocketAddress recipient) {
        super(message, recipient);
    }

    public UdpTestMessage(UdpMessageHead head, InetSocketAddress recipient) {
        super(head, recipient);
    }

    public UdpTestMessage(UdpMessageHead head, InetSocketAddress sender, InetSocketAddress recipient) {
        super(head, sender, recipient);
    }

    @Override
    protected UdpMessageHead doBuild(byte[] message) {
        return new UdpMessageHead();
    }
}
