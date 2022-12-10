package com.dream.iot.handle.proxy;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.core.Ordered;

import java.lang.reflect.Proxy;

/**
 * @see #getOrder() 支持改变执行顺序
 * @see org.springframework.core.annotation.Order
 * @see AutoConfigureBefore 在主应用里面控制加载配置{@link org.springframework.context.annotation.Configuration}的顺序
 * @see org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean 控制如果不生效可以指定配置类的加载顺序 {@link AutoConfigureBefore}
 */
public interface ProtocolHandleProxyMatcher extends Ordered {

    boolean matcher(Object target);

    @Override
    default int getOrder() {
        return 10000;
    }

    default Object createProxy(Object target) {
        final Class<?> targetClass = target.getClass();
        return Proxy.newProxyInstance(targetClass.getClassLoader()
                , targetClass.getInterfaces(), invocationHandler(target));
    }

    ProtocolHandleInvocationHandler invocationHandler(Object target);
}
