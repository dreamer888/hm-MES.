package com.dream.iot.plc;


import com.dream.iot.ProtocolType;

public enum PlcProtocolType implements ProtocolType {

    Omron("欧姆龙"),
    SiemensS7("西门子S7序列"),
    ;

    private String desc;

    PlcProtocolType(String desc) {
        this.desc = desc;
    }

    @Override
    public Enum getType() {
        return this;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
