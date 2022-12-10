package com.dream.iot.test;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

public class IotTestStartListener implements ApplicationListener<ApplicationReadyEvent>, DisposableBean {

    private Thread thread;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationStartedEvent) {

        this.thread = new Thread(() -> {
            applicationStartedEvent.getApplicationContext().getBeanFactory()
                    .getBeanProvider(IotTestHandle.class).orderedStream().forEach(item -> {
                try {
                    item.start();
                    System.out.println();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        this.thread.start();
    }

    @Override
    public void destroy() throws Exception {
        this.thread.interrupt();
    }
}
