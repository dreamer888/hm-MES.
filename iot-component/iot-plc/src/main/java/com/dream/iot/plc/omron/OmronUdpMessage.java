package com.dream.iot.plc.omron;

import com.dream.iot.client.udp.UdpClientMessage;
//import com.dream.iot.plc.PlcClientMessage;

public class OmronUdpMessage extends UdpClientMessage {

    public OmronUdpMessage(byte[] message) {
        super(message);
    }

    public OmronUdpMessage(OmronMessageHeader head) {
        super(head);
    }

    public OmronUdpMessage(OmronMessageHeader head, OmronMessageBody body) {
        super(head, body);
    }

    @Override
    protected MessageHead doBuild(byte[] message) {

        return new OmronMessageHeader(this.getChannelId());
    }


}
