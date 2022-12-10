package com.dream.iot.redis.consumer;

import java.util.List;

public interface BlockConsumerOpera<C extends RedisConsumer, R> extends RedisConsumerOpera<C, R>{


    /**
     * 阻塞的消费列表
     * @return
     */
    List<RedisConsumer> blocks();

    /**
     * 阻塞获取值
     * @param timeout
     * @return
     */
    List invoker(String key, long timeout);
}
