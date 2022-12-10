package com.dream.iot.server;

import com.dream.iot.config.ConnectProperties;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class AbstractUdpServer implements IotSocketServer {

    private ConnectProperties config;

    public AbstractUdpServer(int port) {
        this.config = new ConnectProperties(port);
    }

    public AbstractUdpServer(ConnectProperties config) {
        this.config = config;
    }

    @Override
    public int port() {
        return this.config.getPort();
    }

    @Override
    public ConnectProperties config() {
        return this.config;
    }

    /**
     * 返回设备解码器
     * @return
     */
    public abstract ChannelInboundHandlerAdapter getMessageDecoder();
}
