package com.dream.iot.test.client.line;

import com.dream.iot.ProtocolType;
import com.dream.iot.client.protocol.ServerInitiativeProtocol;
import com.dream.iot.test.ServerInfoUtil;
import com.dream.iot.test.SystemInfo;
import com.dream.iot.test.TestProtocolType;
import com.dream.iot.test.message.line.LineMessageBody;
import com.dream.iot.test.message.line.LineMessageHead;

public class LineServerInitiativeProtocol extends ServerInitiativeProtocol<LineClientMessage> {

    public LineServerInitiativeProtocol(LineClientMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected void doBuildRequestMessage(LineClientMessage requestMessage) {
        SystemInfo payload = requestMessage.getHead().getPayload();
        ServerInfoUtil.printServerInfo(payload);
    }

    @Override
    protected LineClientMessage doBuildResponseMessage() {
        LineClientMessage clientMessage = requestMessage();
        LineMessageHead head = clientMessage.getHead();
        return new LineClientMessage(LineMessageHead.buildHeader(head.getEquipCode()
                , head.getMessageId(), head.getType(), head.getPayload()), LineMessageBody.build());
    }

    @Override
    public ProtocolType protocolType() {
        return TestProtocolType.PIReq;
    }
}
