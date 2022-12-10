package com.dream.iot.test.client.line;

import com.dream.iot.client.protocol.ClientInitiativeProtocol;
import com.dream.iot.test.ServerInfoUtil;
import com.dream.iot.test.SystemInfo;
import com.dream.iot.test.TestProtocolType;
import com.dream.iot.test.message.line.LineMessageBody;
import com.dream.iot.test.message.line.LineMessageHead;

public class LineClientRequestProtocol extends ClientInitiativeProtocol<LineClientMessage> {

    private String deviceSn;

    public LineClientRequestProtocol(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    @Override
    protected LineClientMessage doBuildRequestMessage() {
        String messageId = ServerInfoUtil.getMessageId();
        SystemInfo systemInfo = new SystemInfo(); //ServerInfoUtil.getSystemInfo();
        LineMessageHead messageHead = LineMessageHead.buildHeader(this.deviceSn, messageId, TestProtocolType.CIReq, systemInfo);
        return new LineClientMessage(messageHead, LineMessageBody.build());
    }

    @Override
    public void doBuildResponseMessage(LineClientMessage message) {

    }

    @Override
    public TestProtocolType protocolType() {
        return TestProtocolType.CIReq;
    }
}
