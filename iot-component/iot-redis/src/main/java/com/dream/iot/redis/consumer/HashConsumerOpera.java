package com.dream.iot.redis.consumer;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

public class HashConsumerOpera implements RedisConsumerOpera<HashConsumer, Collection<String>>{

    private List<HashConsumer> consumers;

    public HashConsumerOpera(List<HashConsumer> consumers) {
        this.consumers = consumers;
    }

    @Override
    public List invoker(String key, int size) {
        final Cursor scan = template().boundHashOps(key)
                .scan(ScanOptions.scanOptions().match("*").count(size).build());
        try {
            if(scan.hasNext()) {

            }
        } finally {
//            scan.close();
        }
        return null; //template().boundHashOps(key).scan(ScanOptions.scanOptions().build());
    }

    @Override
    public List deserialize(List<?> value, Class clazz) {
        return null;
    }

    @Override
    public void remove(String key, Collection<String> keys) {
        if(!CollectionUtils.isEmpty(keys)) {
            template().boundHashOps(key).delete(keys);
        }
    }

    @Override
    public List<HashConsumer> consumers() {
        return this.consumers;
    }
}
