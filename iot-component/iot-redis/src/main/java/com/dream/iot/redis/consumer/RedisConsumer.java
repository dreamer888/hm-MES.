package com.dream.iot.redis.consumer;

import com.dream.iot.redis.IotRedis;

import java.util.List;

/**
 * redis 消费者
 * @param <V> 消费的对象
 * @param <R> 消费完之后返回的数据(主要用来要删除的消费后的Key) 如果是List类型返回消费的条数, 如果是Hash类型返回消费后的Key
 */
public interface RedisConsumer<V, R> extends IotRedis {

    /**
     * 返回key
     * @return
     */
    String getKey();

    /**
     *  每次读取的最大条数
     *  可以通过指定此值来指定此消费的线程执行时间: 一般此值越大, 需要执行的时间越长, 其他消费的执行时间会减少
     * @return 条数
     */
    default int maxSize() {
        return 50;
    }

    /**
     * redis 数据消费
     * @return 消费的条数或者消费过的Key等
     */
    R consumer(List<V> v);
}
