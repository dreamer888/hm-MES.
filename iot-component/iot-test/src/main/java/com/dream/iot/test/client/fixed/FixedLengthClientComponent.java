package com.dream.iot.test.client.fixed;

import com.dream.iot.CoreConst;
import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.codec.FixedLengthFrameClient;
import com.dream.iot.client.component.FixedLengthFrameClientComponent;
import com.dream.iot.AbstractProtocol;
import com.dream.iot.test.IotTestProperties;
import com.dream.iot.test.TestProtocolType;
import io.netty.channel.Channel;

public class FixedLengthClientComponent extends FixedLengthFrameClientComponent<FixedLengthClientMessage> {

    public FixedLengthClientComponent(ClientConnectProperties config) {
        super(config, 28);
    }

    @Override
    public String getName() {
        return "固定长度字段解码";
    }

    @Override
    public String getDesc() {
        return "用于测试客户端固定长度解码器[FixedLengthFrameDecoder]";
    }

    @Override
    public TcpSocketClient createNewClient(ClientConnectProperties config) {
        return new FixedLengthFrameClient(this, config, this.getFrameLength()) {
            @Override
            protected void doInitChannel(Channel channel) {
                super.doInitChannel(channel);
                IotTestProperties.TestMultiConnectConfig connectConfig = (IotTestProperties.TestMultiConnectConfig) config;
                channel.attr(CoreConst.EQUIP_CODE).set(Long.valueOf(connectConfig.getDeviceSn()));
            }
        };
    }

    @Override
    public AbstractProtocol getProtocol(FixedLengthClientMessage message) {
        TestProtocolType type = message.getHead().getType();
        if(type == TestProtocolType.CIReq) {
            return remove(message.getHead().getMessageId());
        } else if(type == TestProtocolType.PIReq) {
            return new FixedLengthServerRequestProtocol(message);
        }

        return null;
    }
}
