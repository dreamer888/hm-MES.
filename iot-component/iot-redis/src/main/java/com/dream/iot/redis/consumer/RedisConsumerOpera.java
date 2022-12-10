package com.dream.iot.redis.consumer;

import com.dream.iot.redis.IotRedis;

import java.util.List;

public interface RedisConsumerOpera<C extends RedisConsumer, R> extends IotRedis {

    /**
     * 获取值
     * @return
     */
    List invoker(String key, int size);

    /**
     * 反序列化
     * @param value
     * @param clazz
     * @return
     */
    List deserialize(List<?> value, Class clazz);

    /**
     * 移除已经消费的列表元素
     * @param key
     * @param r
     */
    void remove(String key, R r);

    /**
     * 普通的消费列表
     * @return
     */
    List<C> consumers();
}
