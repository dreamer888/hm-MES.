package com.dream.iot.client;

import com.dream.iot.server.ServerSocketProtocol;

/**
 * 用来处理应用程序客户端和服务端之间的关联关系
 * 应用客户端发起 -> 请求设备服务端 -> 请求设备 -> 设备响应 -> 响应客户端
 */
@Deprecated
public interface ClientProxyRelation<T extends ClientProxyRelation> extends ClientProxySync<T> {

    /**
     * 同步执行
     * @param timeout
     * @return
     */
    T sync(long timeout);

    @Override
    T timeout(long timeout);

    /**
     * 用来声明此次请求是否需要加入超时管理器
     * @see com.dream.iot.AbstractProtocolTimeoutManager
     * @return
     */
    default boolean isRelation() {
        return getTimeout() > 0;
    }

    @Override
    default ServerSocketProtocol getProxyProtocol() {
        throw new UnsupportedOperationException();
    }

    @Override
    default ClientProxySync setProxyProtocol(ServerSocketProtocol protocol) {
        throw new UnsupportedOperationException();
    }
}
