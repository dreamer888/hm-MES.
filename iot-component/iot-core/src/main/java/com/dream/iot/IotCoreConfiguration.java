package com.dream.iot;

import com.dream.iot.codec.filter.CombinedFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.List;

/**
 * create time: 2022/1/16
 *
 * @author dream
 * @since 1.0
 */
@PropertySource("classpath:IOTCore.properties")
@EnableConfigurationProperties(IotCoreProperties.class)
@AutoConfigureBefore(TaskSchedulingAutoConfiguration.class)
public class IotCoreConfiguration {

    private final IotCoreProperties coreProperties;

    public IotCoreConfiguration(IotCoreProperties coreProperties) {
        this.coreProperties = coreProperties;
    }

    @Bean
    public IotThreadManager iotThreadManager() {
        return IotThreadManager.instance();
    }

    @Bean
    public FilterManager filterManager(ObjectProvider<List<CombinedFilter>> filters) {
        return new FilterManager(filters.getIfAvailable());
    }

    /**
     * 自定义调度任务执行器
     * issue：https://gitee.com/iteaj/iot/issues/I5D662
     * @param iotThreadManager
     * @return
     */
    @Bean
    @Lazy
    public ThreadPoolTaskScheduler iotTaskExecutor(IotThreadManager iotThreadManager) {
        IotCoreProperties.IotTaskExecutionProperties properties = coreProperties.getTask();
        if(properties == null) {
            properties = new IotCoreProperties.IotTaskExecutionProperties();
        }

        TaskSchedulerBuilder builder = new TaskSchedulerBuilder();
        builder = builder.poolSize(properties.getCoreSize());
        builder = builder.awaitTermination(properties.isAwaitTermination());
        builder = builder.awaitTerminationPeriod(properties.getAwaitTerminationPeriod());
        builder = builder.threadNamePrefix(properties.getThreadNamePrefix()+properties.getCoreSize()+"-");

        iotThreadManager.setSchedulerExecutor(builder.build());
        return iotThreadManager.getSchedulerExecutor();
    }


}
