package com.dream.iot;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * create time: 2022/1/17
 * 框架准备就绪事件监听
 * @author dreamer
 * @since 1.0
 */
public class IotFrameworkReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        final ConfigurableApplicationContext context = event.getApplicationContext();
        final String version = context.getEnvironment().getProperty("iot.version");
        System.out.println("   ===   --------------------------------------------------------------------------------   =======\n"
                +"    |    |                                                                              |      |\n"
                +"    |    |                     STARTED IOT FRAMEWORK BY NETTY (v"+version+")                  |      |\n"
                +"    |    |                             (welcome to hm IOT)                           |      |\n"
                +"    |    |                              www.dreammm.net  QQ 75039960,   phone 18665802636                                                         |      |\n"
                +"   ===   --------------------------------------------------------------------------------      *");
    }
}
