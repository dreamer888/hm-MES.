package com.dream.iot.test;

import com.dream.iot.server.ServerBootstrapInitializing;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;

public class TestServerInitializing implements ServerBootstrapInitializing {

    @Override
    public void udp(Bootstrap bootstrap) {

    }

    @Override
    public void tcp(ServerBootstrap bootstrap) {
//        bootstrap.option(EpollChannelOption.SO_KEEPALIVE, true)
                // TCP在断开连接之前应发送的最大保持活动探测数
//                .option(EpollChannelOption.TCP_KEEPCNT, 3)
                // 各个keepalive探针之间的发送周期(以秒为单位)
//                .option(EpollChannelOption.TCP_KEEPINTVL, 3)
                // 如果已在套接字上设置套接字选项SO_KEEPALIVE，则在TCP开始发送保持活动探测之前，连接需要保持空闲时间(以秒为单位)
//                .option(EpollChannelOption.TCP_KEEPIDLE, 60)
        ;
    }
}
