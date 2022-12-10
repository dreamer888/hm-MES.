package com.dream.iot.plc.omron;

import com.dream.iot.client.ClientMessage;
import com.dream.iot.client.udp.UdpClientMessage;
import com.dream.iot.udp.UdpMessage;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * 基于Udp协议的客户端报文
 */
public  class OmronUdpClientMessage extends UdpClientMessage {

    public OmronUdpClientMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }


    public OmronUdpClientMessage(OmronMessageHeader head, OmronMessageBody body) {
        super(head, body);
    }

    public OmronUdpClientMessage(OmronMessageHeader head) {
        super(head);
    }


    @Override
    protected MessageHead doBuild(byte[] message) {
        return null;
    }
}
