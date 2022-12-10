package com.dream.iot.handle.proxy;

import com.dream.iot.ProtocolHandle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 只支持接口代理
 */
public class ProtocolProxyHandlePostProcessor implements BeanPostProcessor{

    private List<ProtocolHandleProxyMatcher> matchers;
    private ConfigurableListableBeanFactory beanFactory;
    private ObjectProvider<ProtocolHandleProxyMatcher> beanProvider;

    public ProtocolProxyHandlePostProcessor(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.beanProvider = beanFactory.getBeanProvider(ProtocolHandleProxyMatcher.class);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof ProtocolHandle) {
            if(matchers == null) {
                this.matchers = this.beanProvider.orderedStream().collect(Collectors.toList());
            }

            // 支持一层层代理
            if(this.matchers.size() > 0) {
                Object proxy = bean;
                for (ProtocolHandleProxyMatcher matcher : this.matchers) {
                    if(matcher.matcher(bean)) {
                        proxy = matcher.createProxy(proxy);
                    }
                }

                return proxy;
            } else {
                return bean;
            }
        }

        return bean;
    }

}
