package com.dream.iot.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;

/**
 * @see ServerBootstrap 自定义初始化
 */
public interface ServerBootstrapInitializing {

    /**
     * udp初始化
     * @param bootstrap
     */
    void udp(Bootstrap bootstrap);

    /**
     * tcp初始化
     * @param bootstrap
     */
    void tcp(ServerBootstrap bootstrap);
}
