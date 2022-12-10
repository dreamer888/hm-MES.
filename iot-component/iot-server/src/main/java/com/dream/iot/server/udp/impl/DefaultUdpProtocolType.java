package com.dream.iot.server.udp.impl;

import com.dream.iot.ProtocolType;

public enum DefaultUdpProtocolType implements ProtocolType {
    DEFAULT("udp默认协议");

    private String desc;

    DefaultUdpProtocolType(String desc) {
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
