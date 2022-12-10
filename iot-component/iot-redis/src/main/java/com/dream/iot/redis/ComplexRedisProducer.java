package com.dream.iot.redis;

import com.dream.iot.redis.producer.RedisProducer;
import com.dream.iot.AbstractProtocol;
import org.springframework.data.redis.core.RedisTemplate;

public interface ComplexRedisProducer<T extends AbstractProtocol> extends RedisProducer<T> {

    /**
     * 持久化到redis
     * @param protocol
     */
    void producer(T protocol, RedisTemplate template);
}
