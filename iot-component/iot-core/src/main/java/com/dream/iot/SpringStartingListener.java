package com.dream.iot;

import com.dream.iot.handle.proxy.ProtocolProxyHandlePostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;

public class SpringStartingListener implements ApplicationListener<ApplicationContextInitializedEvent> {

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent applicationContextInitializedEvent) {
        final ConfigurableListableBeanFactory beanFactory = applicationContextInitializedEvent.getApplicationContext().getBeanFactory();
        beanFactory.addBeanPostProcessor(new ProtocolProxyHandlePostProcessor(beanFactory));
    }
}
