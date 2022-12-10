package com.dream.iot.test;

import com.dream.iot.ProtocolType;

public enum TestProtocolType implements ProtocolType {
    // server initiative request
    PIReq("平台主动请求"),

    // client initiative request
    CIReq("客户端主动请求"),

    WillTop("mqtt遗嘱"),

    Heart("心跳"),

    WebSocket_Simple("简单的WebSocket类型")

    ;

    private String desc;

    TestProtocolType(String desc) {
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
