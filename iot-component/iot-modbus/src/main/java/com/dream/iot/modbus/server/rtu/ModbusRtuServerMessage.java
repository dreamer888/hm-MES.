package com.dream.iot.modbus.server.rtu;

import com.dream.iot.server.ServerMessage;
import com.dream.iot.utils.ByteUtil;
import com.dream.iot.modbus.ModbusUtil;

public abstract class ModbusRtuServerMessage extends ServerMessage implements ModbusRtuMessage {

    private String equipCode;

    public ModbusRtuServerMessage(byte[] message) {
        super(message);
    }

    public ModbusRtuServerMessage(String equipCode) {
        super(EMPTY);
        this.equipCode = equipCode;
    }

    protected ModbusRtuServerMessage(ModbusRtuHeader head) {
        super(head);
    }

    protected ModbusRtuServerMessage(ModbusRtuHeader head, ModbusRtuBody body) {
        super(head, body);
    }

    /**
     * 进行CRC计算
     */
    @Override
    public void writeBuild() {
        super.writeBuild();
        String crc = ModbusUtil.getCRC(ByteUtil.subBytes(this.message
                , 0, this.message.length - 2), true);

        byte[] bytes = ByteUtil.hexToByte(crc);
        this.message[this.length() - 1] = bytes[1];
        this.message[this.length() - 2] = bytes[0];
    }

    @Override
    protected ModbusRtuHeader doBuild(byte[] message) {
        this.messageBody = ModbusRtuBody.buildResponseBody(message);

        ModbusRtuHeader rtuHeader = ModbusRtuHeader.buildResponseHeader(message);
        rtuHeader.setEquipCode(this.equipCode);
        rtuHeader.setMessageId(getChannelId());

        // #解决头部没有设置协议类型问题
        rtuHeader.setType(getBody().getCode());
        return rtuHeader;
    }

    public String getEquipCode() {
        return equipCode;
    }

    public void setEquipCode(String equipCode) {
        this.equipCode = equipCode;
    }

    @Override
    public ModbusRtuBody getBody() {
        return (ModbusRtuBody) super.getBody();
    }

    @Override
    public ModbusRtuHeader getHead() {
        return (ModbusRtuHeader) super.getHead();
    }
}
