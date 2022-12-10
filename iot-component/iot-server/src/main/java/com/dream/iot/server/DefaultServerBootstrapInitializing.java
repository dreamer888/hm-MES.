package com.dream.iot.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;

public class DefaultServerBootstrapInitializing implements ServerBootstrapInitializing {

    @Override
    public void udp(Bootstrap bootstrap) {
        // 设置读缓冲区为2M
        bootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 1024)
                // 设置写缓冲区为1M
                .option(ChannelOption.SO_SNDBUF, 1024 * 1024)
                .option(EpollChannelOption.SO_REUSEPORT, true)
                .option(ChannelOption.SO_BROADCAST, true);
    }

    @Override
    public void tcp(ServerBootstrap bootstrap) {
        // tcp 半连接队列的长度
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }
}
