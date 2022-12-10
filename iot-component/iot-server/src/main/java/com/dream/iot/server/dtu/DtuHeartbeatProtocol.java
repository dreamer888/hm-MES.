package com.dream.iot.server.dtu;

import com.dream.iot.Message;
import com.dream.iot.ProtocolType;
import com.dream.iot.server.ServerMessage;
import com.dream.iot.server.dtu.message.DtuMessage;
import com.dream.iot.server.protocol.HeartbeatProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DtuHeartbeatProtocol extends HeartbeatProtocol {

    private static Logger logger = LoggerFactory.getLogger(DtuHeartbeatProtocol.class);

    public DtuHeartbeatProtocol(ServerMessage requestMessage) {
        super(requestMessage);
        if(!(requestMessage instanceof DtuMessage)) {
            throw new DtuProtocolException("不支持的报文类型 期待[DtuMessage]", protocolType());
        }
    }

    @Override
    protected ServerMessage doBuildResponseMessage() {
        return null;
    }

    @Override
    protected void doBuildRequestMessage(ServerMessage requestMessage) {
        if(logger.isTraceEnabled()) {
            Message.MessageHead head = requestMessage.getHead();
            logger.trace("Dtu心跳({}) Dtu编号：{} - messageId：{} - 协议：{}", getServerComponent().getName()
                    , head.getEquipCode(), head.getMessageId(), head.getType());
        }
    }

    @Override
    public ProtocolType protocolType() {
        return DtuCommonProtocolType.HEARTBEAT;
    }
}
