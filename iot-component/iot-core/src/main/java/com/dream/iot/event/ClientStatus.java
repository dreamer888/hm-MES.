package com.dream.iot.event;

/**
 * Create Date By 2017-09-29
 * @author dream
 * @since 1.7
 */
public enum ClientStatus {
    offline, // 掉线
    /**
     * 设备上线处理
     * 1. 如果是服务端的设备必须注册设备编号之后才触发
     * 2. 如果是客户端则必须所有连接完成之后 例如 PLC完成握手协议、mqtt完成ConnectAck
     */
    online; // 上线
}
