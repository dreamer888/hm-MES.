package com.dream.iot.test.server.fixed;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.component.FixedLengthFrameDecoderServerComponent;
import com.dream.iot.server.protocol.HeartbeatProtocol;
import com.dream.iot.test.TestProtocolType;

/**
 * 固定长度解码器组件测试
 */
public class TestFixedLengthDecoderComponent extends FixedLengthFrameDecoderServerComponent<FixedLengthServerMessage> {

    public TestFixedLengthDecoderComponent(ConnectProperties connectProperties) {
        super(connectProperties, 28);
    }

    @Override
    public String getDesc() {
        return "用于测试服务端固定长度字段解码器[FixedLengthFrameDecoder]";
    }

    @Override
    public AbstractProtocol getProtocol(FixedLengthServerMessage message) {
        TestProtocolType protocolType = message.getHead().getType();
        if(protocolType == TestProtocolType.Heart) {
            return HeartbeatProtocol.getInstance(message);
        } else if(protocolType == TestProtocolType.CIReq) {
            return new FixedLengthClientRequestProtocol(message);
        } else {
            return remove(message.getHead().getMessageId());
        }

    }

    @Override
    public String getName() {
        return "固定长度字段解码";
    }

}
