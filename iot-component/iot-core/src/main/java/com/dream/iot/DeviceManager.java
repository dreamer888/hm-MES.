package com.dream.iot;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroup;

import java.util.Optional;

/**
 * <p>设备链接管理</p>
 * 用来管理设备和链接的映射关系
 * Create Date By 2017-09-12
 * @author dream
 * @since 1.7
 */
public interface DeviceManager extends ChannelGroup {

    /**
     * 新增对应的设备
     * @param equipCode 设备编号
     * @param channel 对应的连接
     * @return
     */
    boolean add(String equipCode, Channel channel);

    /**
     * 有报文交互过的客户端数量
     * @return
     */
    int useSize();

    /**
     * 通过设备编号获取对应的{@link Channel}
     * @param equipCode 设备编号
     * @return
     */
    Channel find(String equipCode);

    /**
     * 写出报文
     * @param equipCode 设备编号
     * @param msg 发送的协议
     * @param args 自定义参数
     * @return
     */
    Optional<ChannelFuture> writeAndFlush(String equipCode, Object msg, Object... args);

    /**
     * 写出协议
     * @see Protocol#requestMessage() 请求的报文
     * @see Protocol#responseMessage() 响应的报文
     * @param equipCode 设备编号
     * @param protocol 要写出的协议
     * @return
     */
    Optional<ChannelFuture> writeAndFlush(String equipCode, Protocol protocol);
}
