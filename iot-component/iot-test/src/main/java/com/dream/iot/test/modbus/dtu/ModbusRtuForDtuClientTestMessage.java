package com.dream.iot.test.modbus.dtu;

import com.dream.iot.modbus.client.rtu.ModbusRtuClientMessage;
import com.dream.iot.modbus.server.rtu.ModbusRtuBody;
import com.dream.iot.modbus.server.rtu.ModbusRtuHeader;

public class ModbusRtuForDtuClientTestMessage extends ModbusRtuClientMessage {

    public ModbusRtuForDtuClientTestMessage(byte[] message) {
        super(message);
    }

    public ModbusRtuForDtuClientTestMessage(ModbusRtuHeader head) {
        super(head);
    }

    public ModbusRtuForDtuClientTestMessage(ModbusRtuHeader head, ModbusRtuBody body) {
        super(head, body);
    }

}
