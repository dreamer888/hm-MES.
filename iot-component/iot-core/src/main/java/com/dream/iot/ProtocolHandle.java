package com.dream.iot;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * <p>协议对应的业务处理器</p>
 * Create Date By 2020-09-21
 * @author dream
 * @since 1.8
 */
public interface ProtocolHandle<T extends Protocol> {

    Method method = ReflectionUtils.findMethod(ProtocolHandle.class, "handle", Protocol.class);

    /**
     * 协议的业务处理
     * @param protocol
     */
    Object handle(T protocol);

}
