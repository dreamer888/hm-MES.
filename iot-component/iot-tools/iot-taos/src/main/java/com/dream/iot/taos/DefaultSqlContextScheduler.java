package com.dream.iot.taos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import java.time.Duration;
import java.time.Instant;

public class DefaultSqlContextScheduler implements TaosSqlContextScheduler {

    @Autowired
    private TaskScheduler iotTaskExecutor;

    @Override
    public void scheduler(SqlContext sqlContext) {
        final TaosHandle handle = sqlContext.getTaosHandle();

        // 加入任务调度
        iotTaskExecutor.scheduleWithFixedDelay(sqlContext, Instant.now(), Duration.ofSeconds(handle.period()));
    }
}
