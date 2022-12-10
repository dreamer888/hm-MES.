package com.dream.iot.modbus.consts;

/**
 * create time: 2021/9/11
 *  二进制位状态 0表示关 1表示开
 * @author dream
 * @since 1.0
 */
public enum ModbusBitStatus {

    ON((byte) 0x01),
    OFF((byte) 0x00)
    ;

    private byte bit;

    ModbusBitStatus(byte bit) {
        this.bit = bit;
    }

    public byte getBit() {
        return bit;
    }
}
