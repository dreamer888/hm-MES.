package com.dream.iot.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

public interface IotRedis {

    RedisTemplateWrapper wrapper = new RedisTemplateWrapper();

    default RedisTemplate template() {
        if(wrapper.template == null) {
            throw new IllegalStateException("[RedisTemplate]未完成初始化");
        }

        return wrapper.template;
    }

    /**
     * @see IotRedisConfiguration#redisTemplate(RedisConnectionFactory) 初始化
     */
    class RedisTemplateWrapper {
        public static RedisTemplate template;
    }
}
