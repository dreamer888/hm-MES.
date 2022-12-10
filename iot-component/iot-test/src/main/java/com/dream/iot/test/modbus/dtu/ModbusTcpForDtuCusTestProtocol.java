package com.dream.iot.test.modbus.dtu;

import com.dream.iot.ProtocolType;
import com.dream.iot.modbus.server.dtu.ModbusTcpForDtuMessage;
import com.dream.iot.modbus.server.tcp.ModbusTcpMessageBuilder;
import com.dream.iot.server.protocol.ServerInitiativeProtocol;

import java.io.IOException;

/**
 * 自定义ModbusTcpForDtu协议 测试
 */
public class ModbusTcpForDtuCusTestProtocol extends ServerInitiativeProtocol<ModbusTcpForDtuMessage> {

    private String deviceSn;
    private Integer device;
    private Integer start;
    private Integer num;
    private Number writeValue;

    public ModbusTcpForDtuCusTestProtocol(String deviceSn, int device, int start, int num, Number writeValue) {
        this.num = num;
        this.start = start;
        this.device = device;
        this.deviceSn = deviceSn;
        this.writeValue = writeValue;
    }

    @Override
    protected ModbusTcpForDtuMessage doBuildRequestMessage() throws IOException {
        return ModbusTcpMessageBuilder.buildRead03Message(new ModbusTcpForDtuMessage(this.deviceSn), this.device, this.start, this.num);
    }

    @Override
    protected void doBuildResponseMessage(ModbusTcpForDtuMessage message) { }

    @Override
    public ProtocolType protocolType() {
        return null;
    }

    public Number getWriteValue() {
        return writeValue;
    }
}
