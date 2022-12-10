package com.dream.iot.server.dtu;

import com.dream.iot.ProtocolType;

public enum DtuCommonProtocolType implements ProtocolType {

    AT("AT指令"),
    DTU("DTU私有协议"),
    PASSED("放行"),
    HEARTBEAT("DTU心跳"),
    DEVICE_SN("DTU注册编号")
    ;

    private String desc;

    DtuCommonProtocolType(String desc) {
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
