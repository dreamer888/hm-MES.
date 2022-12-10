package com.dream.iot.handle.proxy;

import com.dream.iot.Protocol;
import com.dream.iot.ProtocolHandle;

import java.util.concurrent.Executor;

public interface ProtocolHandleProxy<T extends Protocol> extends ProtocolHandle<T> {

    /**
     * 过滤是否要执行代理
     * @param entity
     * @param proxy
     * @return true 执行代理  false不执行
     */
    default boolean filter(Object entity, Class<? extends ProtocolHandleProxy> proxy) {
        return true;
    }


    /**
     * 是否需要异步执行
     * @param entity 要处理的数据
     * @param proxy 代理类型
     * @return 不为空将异步发送
     */
    default Executor executor(Object entity, Class<? extends ProtocolHandleProxy> proxy) {
        return null;
    }

    /**
     * 代理handle的异常处理
     * @param e
     * @param entity 要处理的数据
     * @param proxy 代理类型
     * @throws HandleProxyException 如果抛出此异常会直接终止之后的代理调用
     */
    default void exceptionHandle(Object entity, Throwable e, Class<? extends ProtocolHandleProxy> proxy) throws HandleProxyException {

    }
}
