package com.dream.iot.test.server.breaker;

import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.server.protocol.ServerInitiativeProtocol;
import com.dream.iot.test.BreakerProtocolType;
import com.dream.iot.test.MessageCreator;

import java.io.IOException;

/**
 * 切换断路器的开闭状态
 */
public class SwitchStatusProtocol extends ServerInitiativeProtocol<BreakerServerMessage> {

    private String deviceSn;

    public SwitchStatusProtocol(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    /**
     * 构建要发送给断路器的报文
     * @return
     * @throws IOException
     */
    @Override
    protected BreakerServerMessage doBuildRequestMessage() throws IOException {
        DefaultMessageHead messageHead = MessageCreator.buildBreakerHeader(Long.valueOf(this.deviceSn), 0, protocolType());
        return new BreakerServerMessage(messageHead);
    }

    @Override
    protected void doBuildResponseMessage(BreakerServerMessage message) {
        /*设备响应是否切换成功的处理*/
    }

    @Override
    public BreakerProtocolType protocolType() {
        return BreakerProtocolType.SwitchStatus;
    }
}
