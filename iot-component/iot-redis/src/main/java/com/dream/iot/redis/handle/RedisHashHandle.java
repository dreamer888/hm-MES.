package com.dream.iot.redis.handle;

import com.dream.iot.redis.consumer.HashConsumer;
import com.dream.iot.redis.producer.HashProducer;
import com.dream.iot.Protocol;

/**
 * 用来处理Redis Hash数据类型的生产和消费
 * @param <P>
 * @param <V>
 */
public interface RedisHashHandle<P extends Protocol, V> extends HashProducer<P, V>, HashConsumer<V> {

}
