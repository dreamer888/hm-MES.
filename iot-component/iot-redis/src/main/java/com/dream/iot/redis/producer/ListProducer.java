package com.dream.iot.redis.producer;

import com.dream.iot.redis.SimpleRedisProducer;
import com.dream.iot.Protocol;
import org.springframework.data.redis.core.ListOperations;

/**
 * redis list 操作
 * @see ListOperations
 * @param <T>
 */
public interface ListProducer<T extends Protocol> extends SimpleRedisProducer<T, ListOperations> {

    @Override
    default ListOperations operation() {
        return template().opsForList();
    }

    /**
     * 默认实现方式, 往左插入最新记录
     * @param value
     */
    @Override
    default void persistence(Object value) {
        operation().leftPush(getKey(), value);
    }
}
