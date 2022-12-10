package com.dream.iot.test.modbus.dtu;

import com.dream.iot.ProtocolType;
import com.dream.iot.client.protocol.ServerInitiativeProtocol;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.modbus.client.tcp.ModbusTcpClientCommonProtocol;
import com.dream.iot.modbus.client.tcp.ModbusTcpClientMessage;
import com.dream.iot.modbus.server.tcp.ModbusTcpBody;
import com.dream.iot.modbus.server.tcp.ModbusTcpHeader;
import com.dream.iot.test.TestProtocolType;

/**
 * 模拟服务端使用Modbus Tcp协议请求Dtu设备
 */
public class ModbusTcpForDtuClientTestProtocol extends ServerInitiativeProtocol<ModbusTcpForDtuClientTestMessage> {

    private ModbusTcpClientMessage message;

    public ModbusTcpForDtuClientTestProtocol(ModbusTcpForDtuClientTestMessage requestMessage) {
        super(requestMessage);
    }

    @Override
    protected void doBuildRequestMessage(ModbusTcpForDtuClientTestMessage requestMessage) {
        ModbusTcpBody body = ModbusTcpBody.copy(requestMessage().getBody());
        ModbusTcpHeader header = ModbusTcpHeader.copy(requestMessage().getHead());

        ModbusTcpClientCommonProtocol commonProtocol = ModbusTcpClientCommonProtocol.build(header, body);
        commonProtocol.sync(2000).request(protocol -> {
            if(protocol.getExecStatus() == ExecStatus.success) {
                this.message = commonProtocol.responseMessage();
            } else {
                logger.error("modbus over失败");
            }
            return null;
        });

    }

    @Override
    protected ModbusTcpForDtuClientTestMessage doBuildResponseMessage() {
        if(this.message != null) {
            // 恢复NextId
            ModbusTcpHeader head = requestMessage().getHead();
            this.message.getHead().setNextId(head.getNextId());
            this.message.getHead().buildNextId(head.getNextId());
            return new ModbusTcpForDtuClientTestMessage(this.message.getHead(), this.message.getBody());
        } else {
            return null;
        }
    }

    @Override
    public ProtocolType protocolType() {
        return TestProtocolType.PIReq;
    }
}
