package com.dream.iot.codec.filter;

import com.dream.iot.FrameworkComponent;
import org.springframework.core.GenericTypeResolver;

/**
 * 联合客户端注册以及解码过滤
 * @param <C>
 */
public interface CombinedFilter<C extends FrameworkComponent> extends ClientRegister<C>, DecoderFilter<C> {

    CombinedFilter DEFAULT = new CombinedFilter(){};

    @Override
    default Class<C> component() {
        return (Class<C>) GenericTypeResolver
                .resolveTypeArguments(getClass(), CombinedFilter.class)[0];
    }
}
