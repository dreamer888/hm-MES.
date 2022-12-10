package com.dream.iot.taos;

/**
 * 批量数据任务调度
 */
public interface TaosSqlContextScheduler {

    void scheduler(SqlContext sqlContext);
}
