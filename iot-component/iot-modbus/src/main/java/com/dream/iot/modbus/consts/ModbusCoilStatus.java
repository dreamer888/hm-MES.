package com.dream.iot.modbus.consts;

/**
 * 线圈状态
 */
public enum ModbusCoilStatus {
    ON(new byte[] {(byte) 0xFF, 0x00}), // 开
    OFF(new byte[] {0x00, 0x00}) // 关
    ;
    private byte[] code;

    ModbusCoilStatus(byte[] code) {
        this.code = code;
    }

    public byte[] getCode() {
        return code;
    }
}
