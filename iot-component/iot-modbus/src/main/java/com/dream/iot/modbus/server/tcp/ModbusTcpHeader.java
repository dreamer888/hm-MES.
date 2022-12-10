package com.dream.iot.modbus.server.tcp;

import com.dream.iot.ProtocolType;
import com.dream.iot.message.DefaultMessageHead;
import com.dream.iot.utils.ByteUtil;

/**
 * @see ModbusTcpBody
 */
public class ModbusTcpHeader extends DefaultMessageHead {

    private Short nextId; // 事务处理标识(递增)
    private short protocolId = 0x00; // 协议标识 占2byte, 0x00 标识modbus协议

    private short length; // unitId + RTU长度 占2byte
    private byte unitId; // 单元标识 占1byte (从机地址)

    protected ModbusTcpHeader() {
        this(null, null, null);
    }

    protected ModbusTcpHeader(String equipCode, String messageId, ProtocolType type) {
        super(equipCode, messageId, type);
    }

    public static ModbusTcpHeader copy(ModbusTcpHeader head) {
        ModbusTcpHeader header = new ModbusTcpHeader();
        header.length = head.length;
        header.nextId = head.getNextId();
        header.unitId = head.getUnitId();
        header.setType(header.getType());
        header.setEquipCode(head.getEquipCode());
        header.setMessageId(head.getMessageId());
        header.setMessage(head.getMessage() != null ? head.getMessage().clone() : null);
        return header;
    }

    public ModbusTcpHeader buildNextId(short nextId) {
        this.nextId = nextId;
        ByteUtil.addBytes(this.getMessage(), ByteUtil.getBytesOfReverse(nextId), 0); // transactionId
        return this;
    }

    public ModbusTcpHeader buildMessageId() {
        // 可以考虑加上功能码(ModbusCode#getCode())
        this.setMessageId(this.getEquipCode()+":"+this.getUnitId()+":"+this.getNextId());
        return this;
    }

    /**
     * 构建请求头报文头
     * @param unitId 从机设备地址
     * @param nextId 递增的报文id
     * @param rtuLength rtu长度
     * @return
     */
    public static ModbusTcpHeader buildRequestHeader(byte unitId, short nextId, short rtuLength) {
        ModbusTcpHeader modbusTcpHeader = new ModbusTcpHeader();
        modbusTcpHeader.nextId = nextId;
        modbusTcpHeader.length = (short) (1+ rtuLength); // unitId(1) + rtuLength
        modbusTcpHeader.unitId = unitId;

        modbusTcpHeader.setMessage(new byte[2 + 2 + 2 + 1]);// messageId(2) + protocolId(2) + length(2) + unitId(1)
        ByteUtil.addBytes(modbusTcpHeader.getMessage(), ByteUtil.getBytesOfReverse(nextId), 0); // transactionId
        ByteUtil.addBytes(modbusTcpHeader.getMessage(), ByteUtil.getBytesOfReverse(modbusTcpHeader.protocolId), 2); // protocolId
        ByteUtil.addBytes(modbusTcpHeader.getMessage(), ByteUtil.getBytesOfReverse(modbusTcpHeader.length), 4); // length
        ByteUtil.addBytes(modbusTcpHeader.getMessage(), new byte[]{modbusTcpHeader.unitId}, 6); // unitId(deviceSn)

        return modbusTcpHeader;
    }

    /**
     * 构建请求头报文头 nextId自动生成
     * @see #buildNextId(short)
     * @param unitId 从机设备地址
     * @param rtuLength rtu长度
     * @return
     */
    public static ModbusTcpHeader buildRequestHeader(byte unitId, short rtuLength) {
        ModbusTcpHeader modbusTcpHeader = new ModbusTcpHeader();
        modbusTcpHeader.length = (short) (1+ rtuLength); // unitId(1) + rtuLength
        modbusTcpHeader.unitId = unitId;

        modbusTcpHeader.setMessage(new byte[2 + 2 + 2 + 1]);// messageId(2) + protocolId(2) + length(2) + unitId(1)
        // transactionId = #buildNextId(short)
        ByteUtil.addBytes(modbusTcpHeader.getMessage(), ByteUtil.getBytesOfReverse(modbusTcpHeader.protocolId), 2); // protocolId
        ByteUtil.addBytes(modbusTcpHeader.getMessage(), ByteUtil.getBytesOfReverse(modbusTcpHeader.length), 4); // length
        ByteUtil.addBytes(modbusTcpHeader.getMessage(), new byte[]{modbusTcpHeader.unitId}, 6); // unitId(deviceSn)

        return modbusTcpHeader;
    }

    /**
     *  构建
     * @param equipCode
     * @param messageId
     * @param type
     * @return
     */
    public static ModbusTcpHeader buildRequestHeader(String equipCode, String messageId, ProtocolType type) {
        return new ModbusTcpHeader(equipCode, messageId, type);
    }

    public static ModbusTcpHeader buildResponseHeader(byte[] message) {
        ModbusTcpHeader header = new ModbusTcpHeader();

        header.nextId = ByteUtil.bytesToShortOfReverse(message, 0);
        header.length = ByteUtil.bytesToShortOfReverse(message, 4);
        header.unitId = ByteUtil.getByte(message, 6);

        header.setMessage(ByteUtil.subBytes(message, 0, 7));
        return header;
    }

    public Short getNextId() {
        return nextId;
    }

    public void setNextId(Short nextId) {
        this.nextId = nextId;
    }

    public byte getUnitId() {
        return unitId;
    }

    public void setUnitId(byte unitId) {
        this.unitId = unitId;
    }

    public short getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(short protocolId) {
        this.protocolId = protocolId;
    }

    @Override
    public String toString() {
        return "ModbusTcpHeader{" +
                "messageId=" + this.getMessageId() +
                ", protocolId=" + protocolId +
                ", length=" + length +
                ", unitId=" + unitId +
                '}' +
                " HEX=" + (this.getMessage() != null ? ByteUtil.bytesToHexByFormat(this.getMessage()) : "");
    }

}
