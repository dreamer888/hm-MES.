package com.dream.iot.redis.consumer;

import java.util.Collection;
import java.util.List;

public interface HashConsumer<V> extends RedisConsumer<V, Collection<String>> {

    /**
     * @param v
     * @return 返回已经消费过的Key, 用来删除已经消费的数据
     */
    @Override
    Collection<String> consumer(List<V> v);
}
