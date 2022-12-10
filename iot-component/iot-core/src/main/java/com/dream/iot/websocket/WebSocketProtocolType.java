package com.dream.iot.websocket;

import com.dream.iot.ProtocolType;

public enum WebSocketProtocolType implements ProtocolType {

    Close("关闭"),
    Default_Server("默认WebSocket服务端实现"),
    Default_Client("默认WebSocket客户端实现")
    ;

    private String desc;

    WebSocketProtocolType(String desc) {
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
