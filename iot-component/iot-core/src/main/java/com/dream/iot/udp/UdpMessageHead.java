package com.dream.iot.udp;

import com.dream.iot.Message;
import com.dream.iot.ProtocolType;
import com.dream.iot.message.DefaultMessageHead;

public class UdpMessageHead extends DefaultMessageHead {

    public UdpMessageHead() {
        super(Message.EMPTY);
    }

    public UdpMessageHead(byte[] message) {
        super(message);
    }

    public UdpMessageHead(ProtocolType type) {
        super(null, null, type);
    }

    public UdpMessageHead(String equipCode, String messageId, ProtocolType type) {
        super(equipCode, messageId, type);
    }

}
