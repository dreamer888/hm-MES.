package com.dream.iot.proxy;

import com.dream.iot.ProtocolType;

public enum ProxyClientType implements ProtocolType {
    Proxy_Client_Heart("代理客户端心跳协议"),
    Proxy_Client_Server("代理客户端业务协议");

    private String desc;

    ProxyClientType(String desc) {
        this.desc = desc;
    }

    @Override
    public ProxyClientType getType() {
        return this;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

    public static ProxyClientType getInstance(String type) {
        return ProxyClientType.valueOf(type);
    }
}
