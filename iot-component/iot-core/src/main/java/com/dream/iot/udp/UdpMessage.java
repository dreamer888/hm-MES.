package com.dream.iot.udp;

import com.dream.iot.Message;

import java.net.InetSocketAddress;

public interface UdpMessage extends Message {

    /**
     * 发送者地址
     * @return
     */
    InetSocketAddress getSender();

    UdpMessage setSender(InetSocketAddress sender);

    /**
     * 接收者地址
     * @return
     */
    InetSocketAddress getRecipient();

    UdpMessage setRecipient(InetSocketAddress recipient);
}
