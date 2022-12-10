package com.dream.iot.client.component;

import com.dream.iot.client.*;
import com.dream.iot.client.udp.UdpClientConnectProperties;
import com.dream.iot.client.udp.UdpClientMessage;
import io.netty.channel.socket.DatagramPacket;

/**
 * 基于udp实现的客户端
 */
public abstract class UdpClientComponent<M extends UdpClientMessage> extends SocketClientComponent<M, DatagramPacket> {

    public UdpClientComponent(UdpClientConnectProperties config) {
        super(config);
    }

    public UdpClientComponent(UdpClientConnectProperties config, MultiClientManager clientManager) {
        super(config, clientManager);
    }

    @Override
    public void init(Object ...args) {
        this.getClient().init(args[0]);
    }

    public abstract UdpSocketClient createNewClient(ClientConnectProperties config);

    @Override
    public SocketClient createNewClientAndConnect(ClientConnectProperties config) {
        if(!(config instanceof UdpClientConnectProperties)) {
            throw new IllegalArgumentException("Udp协议的客户端组件只支持配置类型[UdpClientConnectProperties]");
        }

        return super.createNewClientAndConnect(config);
    }
}
