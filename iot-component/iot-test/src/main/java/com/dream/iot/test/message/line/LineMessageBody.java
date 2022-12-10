package com.dream.iot.test.message.line;

import com.dream.iot.test.message.TMessageBody;

public class LineMessageBody extends TMessageBody {
    protected LineMessageBody(byte[] message) {
        super(message);
    }

    public static LineMessageBody build() {
        return new LineMessageBody("\n".getBytes());
    }
}
