package com.dream.iot.test.modbus.dtu;

import com.dream.iot.ProtocolType;
import com.dream.iot.client.protocol.ServerInitiativeProtocol;
import com.dream.iot.consts.ExecStatus;
import com.dream.iot.modbus.client.tcp.ModbusTcpClientCommonProtocol;
import com.dream.iot.modbus.client.tcp.ModbusTcpClientMessage;
import com.dream.iot.modbus.server.rtu.ModbusRtuBody;
import com.dream.iot.modbus.server.rtu.ModbusRtuHeader;
import com.dream.iot.modbus.server.tcp.ModbusTcpBody;
import com.dream.iot.modbus.server.tcp.ModbusTcpHeader;
import com.dream.iot.test.TestProtocolType;
import com.dream.iot.modbus.ModbusRtuOverTcpUtils;

/**
 * 模拟Dtu设备请求服务端使用Modbus Rtu协议
 */
public class ModbusRtuForDtuClientTestProtocol extends ServerInitiativeProtocol<ModbusRtuForDtuClientTestMessage> {

    private ModbusTcpClientMessage tcpClientMessage;

    public ModbusRtuForDtuClientTestProtocol(ModbusRtuForDtuClientTestMessage requestMessage) {
        super(requestMessage);
    }

    /**
     * 由于Modbus Rtu使用串口通信, 所以接收到服务端的请求后转成Modbus Tcp协议请求Modbus Tcp服务
     * @see ModbusTcpClientCommonProtocol
     * @param requestMessage
     */
    @Override
    protected void doBuildRequestMessage(ModbusRtuForDtuClientTestMessage requestMessage) {
        requestMessage.getHead().setMessageId(null);
        ModbusTcpBody modbusTcpBody = ModbusRtuOverTcpUtils.toTcpBody(requestMessage.getBody());
        ModbusTcpHeader modbusTcpHeader = ModbusRtuOverTcpUtils.toTcpHeader(requestMessage.getHead(), (short) modbusTcpBody.getLength());

        ModbusTcpClientCommonProtocol commonProtocol = ModbusTcpClientCommonProtocol.build(modbusTcpHeader, modbusTcpBody);
        commonProtocol.sync(2000).request(protocol -> {
            if(protocol.getExecStatus() == ExecStatus.success) {
                this.tcpClientMessage = commonProtocol.responseMessage();
            } else {
                logger.error("modbus over失败");
            }
            return null;
        });

    }

    @Override
    protected ModbusRtuForDtuClientTestMessage doBuildResponseMessage() {
        if(this.tcpClientMessage != null) {
            ModbusRtuBody rtuBody = ModbusRtuOverTcpUtils.toRtuBody(this.tcpClientMessage.getBody());
            ModbusRtuHeader rtuHeader = ModbusRtuOverTcpUtils.toRtuHeader(this.tcpClientMessage.getHead());

            return new ModbusRtuForDtuClientTestMessage(rtuHeader, rtuBody);
        }

        return null;
    }

    @Override
    public ProtocolType protocolType() {
        return TestProtocolType.PIReq;
    }
}
