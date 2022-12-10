package com.dream.iot.client.component;

import com.dream.iot.client.ClientConnectProperties;
import com.dream.iot.client.TcpSocketClient;
import com.dream.iot.client.ClientMessage;
import com.dream.iot.client.MultiClientManager;
import io.netty.buffer.ByteBuf;

public abstract class TcpClientComponent<M extends ClientMessage> extends SocketClientComponent<M, ByteBuf> {

    public TcpClientComponent() { }

    /**
     * @param config 默认客户端配置
     */
    public TcpClientComponent(ClientConnectProperties config) {
        super(config);
    }

    public TcpClientComponent(ClientConnectProperties config, MultiClientManager clientManager) {
        super(config, clientManager);
    }

    public abstract TcpSocketClient createNewClient(ClientConnectProperties config);
}
