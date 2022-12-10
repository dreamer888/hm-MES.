package com.dream.iot.server.dtu;

import com.dream.iot.ProtocolType;
import com.dream.iot.server.ServerMessage;
import com.dream.iot.server.protocol.ClientInitiativeProtocol;

public class DtuDeviceSnProtocol extends ClientInitiativeProtocol<ServerMessage> {

    public DtuDeviceSnProtocol(ServerMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected ServerMessage doBuildResponseMessage() {
        return null;
    }

    @Override
    protected void doBuildRequestMessage(ServerMessage requestMessage) {
        if(DtuConsts.logger.isDebugEnabled()) {
            DtuConsts.logger.debug("DTU设备 {} - 设备编号: {} - 协议类型: {} - 报文: {}"
                    , requestMessage.getHead().getEquipCode(), protocolType().getDesc(), protocolType(), requestMessage);
        }
    }

    @Override
    public ProtocolType protocolType() {
        return DtuCommonProtocolType.DEVICE_SN;
    }
}
