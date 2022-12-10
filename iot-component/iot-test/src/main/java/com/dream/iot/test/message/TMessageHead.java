package com.dream.iot.test.message;

import com.dream.iot.ProtocolType;
import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.test.TestProtocolType;

public class TMessageHead extends DefaultMessageHead {

    public TMessageHead(byte[] message) {
        super(message);
    }

    public TMessageHead(String messageId, String equipCode, ProtocolType tradeType) {
        super(messageId, equipCode, tradeType);
    }

    @Override
    public TestProtocolType getType() {
        return (TestProtocolType) super.getType();
    }
}
