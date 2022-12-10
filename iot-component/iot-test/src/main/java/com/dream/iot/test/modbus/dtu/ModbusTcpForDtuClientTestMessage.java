package com.dream.iot.test.modbus.dtu;

import com.dream.iot.modbus.server.tcp.ModbusTcpBody;
import com.dream.iot.modbus.server.tcp.ModbusTcpHeader;
import com.dream.iot.modbus.client.tcp.ModbusTcpClientMessage;

public class ModbusTcpForDtuClientTestMessage extends ModbusTcpClientMessage {

    public ModbusTcpForDtuClientTestMessage(byte[] message) {
        super(message);
    }

    public ModbusTcpForDtuClientTestMessage(ModbusTcpHeader head, ModbusTcpBody body) {
        super(head, body);
    }
}
