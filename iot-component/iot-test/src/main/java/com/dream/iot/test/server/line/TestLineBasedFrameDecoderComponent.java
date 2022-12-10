package com.dream.iot.test.server.line;

import com.dream.iot.AbstractProtocol;
import com.dream.iot.config.ConnectProperties;
import com.dream.iot.server.component.LineBasedFrameDecoderServerComponent;
import com.dream.iot.server.protocol.HeartbeatProtocol;
import com.dream.iot.test.TestProtocolType;
import com.dream.iot.test.message.TMessageHead;
import com.dream.iot.test.message.line.LineMessage;
import com.dream.iot.test.server.line.protocol.LineClientInitiativeProtocol;

/**
 * 测试基于换行符的解码器 + 网关组件
 * @see LineBasedFrameDecoderServerComponent
 */
public class TestLineBasedFrameDecoderComponent extends LineBasedFrameDecoderServerComponent<LineMessage> {

    public TestLineBasedFrameDecoderComponent(ConnectProperties connectProperties) {
        super(connectProperties, 5 * 1024);
    }

    @Override
    public AbstractProtocol getProtocol(LineMessage message) {
        TMessageHead head = message.getHead();
        // 心跳报文
        if(head.getType() == TestProtocolType.Heart) {
            return HeartbeatProtocol.getInstance(message);

            // 此请求属于客户端响应平台
        } else  if(head.getType() == TestProtocolType.PIReq) {
            return remove(head.getMessageId());

            // 客户端主动请求平台的协议
        } else {
            return new LineClientInitiativeProtocol(message);
        }
    }

    @Override
    public String getDesc() {
        return "测试服务端换行符解码器";
    }

    @Override
    public String getName() {
        return "换行符解码";
    }

}
