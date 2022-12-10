package com.dream.iot.modbus.server.tcp;

import com.dream.iot.server.ServerMessage;

public abstract class ModbusTcpServerMessage extends ServerMessage implements ModbusTcpMessage {

    private String equipCode;

    public ModbusTcpServerMessage(byte[] message) {
        super(message);
    }

    public ModbusTcpServerMessage(String equipCode) {
        super(EMPTY);
        this.equipCode = equipCode;
    }

    protected ModbusTcpServerMessage(ModbusTcpHeader head) {
        super(head);
    }

    protected ModbusTcpServerMessage(ModbusTcpHeader head, ModbusTcpBody body) {
        super(head, body);
    }

    @Override
    protected ModbusTcpHeader doBuild(byte[] message) {
        this.messageBody = ModbusTcpBody.buildResponseBody(message);

        ModbusTcpHeader modbusTcpHeader = ModbusTcpHeader.buildResponseHeader(message);
        modbusTcpHeader.setEquipCode(this.equipCode);
        // #解决头部没有设置协议类型问题
        modbusTcpHeader.setType(getBody().getCode());

        return modbusTcpHeader.buildMessageId();
    }

    public String getEquipCode() {
        return equipCode;
    }

    public void setEquipCode(String equipCode) {
        this.equipCode = equipCode;
    }

    @Override
    public ModbusTcpBody getBody() {
        return (ModbusTcpBody) super.getBody();
    }

    @Override
    public ModbusTcpHeader getHead() {
        return (ModbusTcpHeader) super.getHead();
    }
}
