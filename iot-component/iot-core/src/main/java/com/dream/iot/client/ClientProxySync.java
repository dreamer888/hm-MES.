package com.dream.iot.client;

import com.dream.iot.ProtocolException;
import com.dream.iot.ProtocolPreservable;
import com.dream.iot.server.ServerSocketProtocol;

/**
 * 用来代理应用客户端去调用设备或其他客户端
 * @param <T>
 */
public interface ClientProxySync<T extends ClientProxySync> extends ProtocolPreservable {

    /**
     * 发起请求
     * @throws ProtocolException
     */
    void request() throws ProtocolException;

    /**
     * 此次协议是否由客户端协议发起并且调用的
     * @return
     */
    boolean isClientStart();

    T setClientStart(boolean start);

    /**
     * 返回应用客户端主动发起的协议
     * @return
     */
    ServerSocketProtocol getProxyProtocol();

    ClientProxySync setProxyProtocol(ServerSocketProtocol protocol);
}
