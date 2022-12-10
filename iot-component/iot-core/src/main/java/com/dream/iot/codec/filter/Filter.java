package com.dream.iot.codec.filter;

import com.dream.iot.FrameworkComponent;
import org.springframework.core.GenericTypeResolver;

/**
 * 组件过滤器, 用于过滤连接的各个环节
 * @param <C>
 */
public interface Filter<C extends FrameworkComponent> {

    default Class<C> component() {
        return (Class<C>) GenericTypeResolver
                .resolveTypeArguments(getClass(), Filter.class)[0];
    }
}
