package com.dream.iot.modbus.consts;

public enum ModbusErrCode {
    E01((byte) 0x01, "非法功能"),
    E02((byte) 0x02, "非法数据地址"),
    E03((byte) 0x03, "非法数据值"),
    E04((byte) 0x04, "从站设备故障"),
    E05((byte) 0x05, "确认"),
    E06((byte) 0x06, "从属设备忙"),
    E07((byte) 0x07, "从属设备忙"),
    E08((byte) 0x08, "存储奇偶性差错"),
    E0A((byte) 0x0A, "不可用网关路径"),
    E0B((byte) 0x0B, "网关目标设备响应失败")
    ;

    private byte code;
    private String desc;

    ModbusErrCode(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ModbusErrCode valueOf(int code) {
        switch (code) {
            case 0x01: return E01;
            case 0x02: return E02;
            case 0x03: return E03;
            case 0x04: return E04;
            case 0x05: return E05;
            case 0x06: return E06;
            case 0x07: return E07;
            case 0x08: return E08;
            case 0x0A: return E0A;
            case 0x0B: return E0B;

            default: throw new IllegalStateException("未知错误码["+code+"]");
        }
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
