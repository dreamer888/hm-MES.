package com.dream.iot.plc.omron;

import com.dream.iot.client.udp.UdpClientMessage;
import com.dream.iot.plc.PlcClientMessage;

/**
 * 基于Udp协议的客户端报文
 */
public  class OmronPlcClientMessage extends PlcClientMessage {

    public OmronPlcClientMessage(MessageHead head, MessageBody body) {
        super(head, body);
    }


    public OmronPlcClientMessage(OmronMessageHeader head, OmronMessageBody body) {
        super(head, body);
    }

    public OmronPlcClientMessage(OmronMessageHeader head) {
        super(head);
    }


    @Override
    protected MessageHead doBuild(byte[] message) {
        return null;
    }
}
