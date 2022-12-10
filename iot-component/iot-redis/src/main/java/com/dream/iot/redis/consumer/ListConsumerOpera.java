package com.dream.iot.redis.consumer;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ListConsumerOpera implements BlockConsumerOpera<ListConsumer, Integer> {

    private List<ListConsumer> consumers;

    public ListConsumerOpera(List<ListConsumer> consumers) {
        this.consumers = consumers;
    }

    @Override
    public List invoker(String key, long timeout) {
        Object o = template().opsForList().rightPop(key, timeout, TimeUnit.SECONDS);
        if(o != null) {
            return Arrays.asList(o);
        } else {
            return null;
        }
    }

    @Override
    public List invoker(String key, int size) {
        return template().opsForList().range(key, 0, size);
    }

    @Override
    public List deserialize(List<?> value, Class clazz) {
        if(CollectionUtils.isEmpty(value)) {
            return null;
        } else {
            return (List) value.stream().map(item -> {
                if(item instanceof JSONObject) {
                    return ((JSONObject) item).toJavaObject(clazz);
                } else {
                    return item;
                }
            }).collect(Collectors.toList());
        }
    }

    @Override
    public void remove(String key, Integer consumerNum) {
        if(consumerNum != null) {
            template().opsForList().trim(key, consumerNum, -1);
        } else {
//            日志提示
        }
    }

    @Override
    public List<RedisConsumer> blocks() {
        return consumers.stream().filter(item -> item.block()).collect(Collectors.toList());
    }

    @Override
    public List<ListConsumer> consumers() {
        return consumers.stream().filter(item -> !item.block()).collect(Collectors.toList());
    }
}
