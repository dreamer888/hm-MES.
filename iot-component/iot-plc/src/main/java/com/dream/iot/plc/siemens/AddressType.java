package com.dream.iot.plc.siemens;

public enum AddressType {

    Bit((byte) 0x01), // 按位读取
    Word((byte) 0x02) // 按字读取
    ;
    private byte type;

    AddressType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }
}
