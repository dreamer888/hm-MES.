package com.dream.iot.redis;

import com.dream.iot.redis.producer.RedisProducer;
import com.dream.iot.AbstractProtocol;
import com.dream.iot.Protocol;

/**
 * @see AbstractProtocol
 * 一种协议只支持单个Key
 * @param <T>
 */
public interface SimpleRedisProducer<T extends Protocol, O> extends RedisProducer<T> {

    /**
     * 返回 redis key
     * @return
     */
    String getKey();

    O operation();
}
