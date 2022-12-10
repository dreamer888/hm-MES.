package com.dream.iot.modbus.server.dtu;

import com.dream.iot.ProtocolType;
import com.dream.iot.modbus.server.rtu.ModbusRtuBody;
import com.dream.iot.modbus.server.rtu.ModbusRtuHeader;
import com.dream.iot.modbus.server.rtu.ModbusRtuServerMessage;
import com.dream.iot.server.dtu.DtuCommonProtocolType;
import com.dream.iot.server.dtu.message.DtuMessage;

public class ModbusRtuForDtuMessage extends ModbusRtuServerMessage implements DtuMessage {

    private ProtocolType protocolType;

    public ModbusRtuForDtuMessage(String equipCode) {
        super(equipCode);
    }

    public ModbusRtuForDtuMessage(byte[] message) {
        super(message);
    }

    public ModbusRtuForDtuMessage(ModbusRtuHeader head) {
        super(head);
    }

    public ModbusRtuForDtuMessage(ModbusRtuHeader head, ModbusRtuBody body) {
        super(head, body);
    }

    @Override
    protected ModbusRtuHeader doBuild(byte[] message) {
        if(getProtocolType() != null) {
            return ModbusRtuHeader.buildRequestHeader(this.getEquipCode(), getChannelId(), getProtocolType());
        }

        return super.doBuild(message);
    }

    @Override
    public ModbusRtuHeader buildFirstHead() {
        return ModbusRtuHeader.buildRequestHeader(this.getEquipCode(), getChannelId(), DtuCommonProtocolType.DEVICE_SN);
    }

    @Override
    public ProtocolType getProtocolType() {
        return protocolType;
    }

    @Override
    public ModbusRtuForDtuMessage setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
        return this;
    }
}
