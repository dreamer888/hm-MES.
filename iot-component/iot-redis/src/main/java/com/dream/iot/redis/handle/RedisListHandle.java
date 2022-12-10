package com.dream.iot.redis.handle;

import com.dream.iot.redis.consumer.ListConsumer;
import com.dream.iot.redis.producer.ListProducer;
import com.dream.iot.Protocol;
import com.dream.iot.ProtocolHandle;
import org.springframework.data.redis.core.ListOperations;

import java.util.List;

/**
 * Redis List opera
 * @see ListOperations
 * @param <T>
 * @param <V>
 */
public interface RedisListHandle<T extends Protocol, V> extends ProtocolHandle<T>, ListProducer<T>, ListConsumer<V> {

    /**
     * 注意：如果返回null则记录不会从redis删除
     * @param vs
     * @return 消费的条数 如果返回null则记录不会从redis删除
     */
    @Override
    Integer consumer(List<V> vs);

    @Override
    Object handle(T protocol);

    @Override
    default ListOperations operation() {
        return template().opsForList();
    }
}
