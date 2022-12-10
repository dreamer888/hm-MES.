package com.dream.iot.plc.omron;

import com.dream.iot.plc.PlcClientMessage;

public class OmronMessage extends PlcClientMessage {

    public OmronMessage(byte[] message) {
        super(message);
    }

    public OmronMessage(OmronMessageHeader head) {
        super(head);
    }

    public OmronMessage(OmronMessageHeader head, OmronMessageBody body) {
        super(head, body);
    }

    @Override
    protected MessageHead doBuild(byte[] message) {

        return new OmronMessageHeader(this.getChannelId());
    }

}
