package com.dream.iot.test.modbus;

import com.dream.iot.modbus.client.tcp.ModbusTcpClientMessage;

public class ModbusTestMessage extends ModbusTcpClientMessage {

    public ModbusTestMessage(byte[] message) {
        super(message);
    }

    @Override
    public String getEquipCode() {
        return null;
    }
}
