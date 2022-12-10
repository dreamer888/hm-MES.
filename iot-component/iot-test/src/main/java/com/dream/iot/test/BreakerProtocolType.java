package com.dream.iot.test;

import com.dream.iot.ProtocolType;

/**
 * 断路器协议类型
 */
public enum BreakerProtocolType implements ProtocolType {

    PushData((byte) 0x01, "数据上报"),
    SwitchStatus((byte) 0x02, "切换状态"),
    ;

    public byte code;
    public String desc;

    BreakerProtocolType(byte code, String desc) {
        this.code = code;
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

    public static BreakerProtocolType getInstance(int code) {
        switch (code) {
            case 0x01: return PushData;
            default: throw new IllegalStateException("不支持的协议码["+code+"]");
        }
    }
}
