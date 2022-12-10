package com.dream.iot.plc.siemens;

import com.dream.iot.plc.PlcClientMessage;

public class SiemensS7Message extends PlcClientMessage {

    public SiemensS7Message(byte[] message) {
        super(message);
    }

    public SiemensS7Message(SiemensMessageHeader head) {
        super(head);
    }

    public SiemensS7Message(SiemensMessageHeader head, MessageBody body) {
        super(head, body);
    }

    @Override
    protected MessageHead doBuild(byte[] message) {
        return new SiemensMessageHeader(getChannelId());
    }
}
