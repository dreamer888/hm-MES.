package com.dream.iot.test.server.breaker;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.component.LengthFieldBasedFrameDecoderServerComponent;
import com.dream.iot.test.BreakerProtocolType;

import java.nio.ByteOrder;

public class BreakerServerComponent extends LengthFieldBasedFrameDecoderServerComponent<BreakerServerMessage> {

    public BreakerServerComponent(ConnectProperties connectProperties) {
        super(connectProperties, ByteOrder.LITTLE_ENDIAN, 256, 0
                , 4, 0, 0, true);
    }

    @Override
    public String getDesc() {
        return "断路器设备服务端";
    }

    @Override
    public AbstractProtocol getProtocol(BreakerServerMessage message) {
        BreakerProtocolType type = message.getHead().getType();

        if(type == BreakerProtocolType.PushData) {
            return new DataAcceptProtocol(message);
        }

        return null;
    }

    @Override
    public String getName() {
        return "用来对接断路器设备";
    }
}
