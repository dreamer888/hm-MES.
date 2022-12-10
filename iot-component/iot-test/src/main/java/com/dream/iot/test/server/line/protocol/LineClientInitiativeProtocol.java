package com.dream.iot.test.server.line.protocol;

import com.dream.iot.server.component.LineBasedFrameDecoderServerComponent;
import com.dream.iot.server.protocol.ClientInitiativeProtocol;
import com.dream.iot.test.TestProtocolType;
import com.dream.iot.test.message.line.LineMessage;
import com.dream.iot.test.message.line.LineMessageBody;
import com.dream.iot.test.message.line.LineMessageHead;

/**
 * 测试{@link LineBasedFrameDecoderServerComponent}组件
 * 属于客户端主动请求服务端请求协议
 * @see TestProtocolType#CIReq
 */
public class LineClientInitiativeProtocol extends ClientInitiativeProtocol<LineMessage> {

    public LineClientInitiativeProtocol(LineMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected LineMessage doBuildResponseMessage() {
        LineMessageHead head = requestMessage.getHead();
        return new LineMessage(LineMessageHead.buildHeader(head.getEquipCode(), head.getMessageId()
                , head.getType(), head.getPayload()), LineMessageBody.build());
    }

    @Override
    protected void doBuildRequestMessage(LineMessage requestMessage) { }


    @Override
    public TestProtocolType protocolType() {
        return TestProtocolType.CIReq;
    }
}
