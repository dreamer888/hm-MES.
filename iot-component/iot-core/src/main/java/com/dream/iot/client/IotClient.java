package com.dream.iot.client;

import java.util.function.Consumer;

public interface IotClient<T> {

    int getPort();

    String getHost();

    void init(T arg);

    /**
     * 连接远程服务器
     * @param consumer 连接回调
     * @param timeout (毫秒) 连接超时时间 0表示不同步
     */
    Object connect(Consumer<?> consumer, long timeout);

    /**
     * 断开连接
     * @param remove 是否移除 如果{@code true}将直接移除此连接  {@code false}将等待断线重连
     */
    Object disconnect(boolean remove);

    /**
     * 返回此客户端对应的组件
     * @return
     */
    ClientComponent getClientComponent();
}
