package com.dream.iot.test.server.udp;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.server.component.DatagramPacketDecoderServerComponent;
import com.dream.iot.test.IotTestProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * create time: 2021/9/12
 *
 * @author dream
 * @since 1.0
 */
@Component
@ConditionalOnProperty(prefix = "iot.test", name = "udp.start", havingValue = "true")
public class TestUdpDecoderComponent extends DatagramPacketDecoderServerComponent<UdpTestMessage> {

    public TestUdpDecoderComponent(IotTestProperties properties) {
        super(properties.getUdp());
    }

    @Override
    public String getDesc() {
        return "测试UDP协议";
    }

    @Override
    public AbstractProtocol getProtocol(UdpTestMessage message) {
        return new UdpTestClientProtocol(message);
    }

    @Override
    public String getName() {
        return "UDP";
    }

    @Override
    public UdpTestMessage createMessage(byte[] message) {
        return new UdpTestMessage(message);
    }
}
