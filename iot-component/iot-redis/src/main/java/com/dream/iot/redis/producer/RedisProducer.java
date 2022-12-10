package com.dream.iot.redis.producer;

import com.dream.iot.handle.proxy.ProtocolHandleProxy;
import com.dream.iot.redis.IotRedis;
import com.dream.iot.Protocol;

import java.util.List;

/**
 * 数据生产者
 * @param <T>
 */
public interface RedisProducer<T extends Protocol> extends IotRedis, ProtocolHandleProxy<T> {

    /**
     * redis 数据生产者
     * @see com.dream.iot.redis.consumer.RedisConsumer#consumer(List) 将由此方法消费
     * @param protocol
     * @return 将直接序列化到redis
     */
    Object handle(T protocol);

    /**
     * 持久化到redis
     * @param value
     */
    void persistence(Object value);
}
