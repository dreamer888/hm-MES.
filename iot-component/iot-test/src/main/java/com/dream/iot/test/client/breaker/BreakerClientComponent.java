package com.dream.iot.test.client.breaker;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.client.component.LengthFieldBasedFrameClientComponent;
import com.dream.iot.test.IotTestProperties;

import java.nio.ByteOrder;

/**
 * 断路器设备模拟组件
 * 使用长度字段解码器解码
 */
public class BreakerClientComponent extends LengthFieldBasedFrameClientComponent<BreakerClientMessage> {

    public BreakerClientComponent(IotTestProperties.BreakerConnectConfig config) {
        super(config, ByteOrder.LITTLE_ENDIAN, 256
                , 0, 4,0, 0, true);
    }

    @Override
    public String getName() {
        return "断路器设备模拟";
    }

    @Override
    public String getDesc() {
        return "用于模拟设备控制, Redis、Taos等数据采集存储测试";
    }

    @Override
    public AbstractProtocol getProtocol(BreakerClientMessage message) {
        return remove(message.getHead().getMessageId());
    }
}
