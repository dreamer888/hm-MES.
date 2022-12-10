package com.dream.iot.server;

import com.dream.iot.config.ConnectProperties;
import io.netty.buffer.ByteBuf;

/**
 * 设备的服务端组件基于TCP协议
 * 一种类型的设备(需要独立端口的)对应一套服务端组件
 */
public abstract class TcpServerComponent<M extends ServerMessage> extends SocketServerComponent<M, ByteBuf> {

    public TcpServerComponent(ConnectProperties connectProperties) {
        super(connectProperties);
    }
}
