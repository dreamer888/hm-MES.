package com.dream.iot.test.server.line.protocol;

import com.dream.iot.ProtocolType;
import com.dream.iot.server.protocol.ServerInitiativeProtocol;
import com.dream.iot.test.ServerInfoUtil;
import com.dream.iot.test.SystemInfo;
import com.dream.iot.test.TestProtocolType;
import com.dream.iot.test.message.line.LineMessage;
import com.dream.iot.test.message.line.LineMessageBody;
import com.dream.iot.test.message.line.LineMessageHead;

import java.io.IOException;

public class LineServerInitiativeProtocol extends ServerInitiativeProtocol<LineMessage> {

    private String equipCode;

    public LineServerInitiativeProtocol(String equipCode) {
        this.equipCode = equipCode;
    }

    @Override
    protected LineMessage doBuildRequestMessage() throws IOException {
        String messageId = ServerInfoUtil.getMessageId();
        SystemInfo systemInfo = ServerInfoUtil.getSystemInfo();
        return new LineMessage(LineMessageHead.buildHeader(this.equipCode
                , messageId, protocolType(), systemInfo), LineMessageBody.build());
    }

    @Override
    protected void doBuildResponseMessage(LineMessage message) {

    }

    @Override
    public ProtocolType protocolType() {
        return TestProtocolType.PIReq;
    }
}
