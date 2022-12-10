package com.dream.iot.redis.producer;

import com.dream.iot.redis.SimpleRedisProducer;
import com.dream.iot.Protocol;
import org.springframework.data.redis.core.HashOperations;

import java.util.Map;

/**
 * Redis Hash格式数据类型生产对象
 * @param <T> 指定此生产者要生产哪个协议生成的数据
 * @param <E> 协议生成的数据
 */
public interface HashProducer<T extends Protocol, E> extends SimpleRedisProducer<T, HashOperations> {

    @Override
    default HashOperations operation() {
        return template().opsForHash();
    }

    @Override
    default void persistence(Object value) {
        operation().put(getKey(), hashKey((E) value), value);
    }

    /**
     * 写入多条数据
     * @param values
     */
    default void persistence(Map values) {
        operation().putAll(getKey(), values);
    }

    /**
     * hash key
     * @param value
     * @return
     */
    String hashKey(E value);
}
