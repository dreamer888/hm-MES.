package com.dream.iot.redis.consumer;

import java.util.List;

public interface ListConsumer<V> extends RedisConsumer<V, Integer>, BlockConsumer {

    /**
     *
     * @param vs
     * @return 返回消费成功的条数, 如果返回null则不删除任何数据, 会导致重复消费
     */
    @Override
    Integer consumer(List<V> vs);

}
