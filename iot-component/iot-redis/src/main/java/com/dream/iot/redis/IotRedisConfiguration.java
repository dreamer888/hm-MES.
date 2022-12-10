package com.dream.iot.redis;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.dream.iot.redis.consumer.ListConsumer;
import com.dream.iot.redis.consumer.ListConsumerOpera;
import com.dream.iot.redis.consumer.RedisConsumerOpera;
import com.dream.iot.redis.consumer.RedisConsumerOperaManager;
import com.dream.iot.redis.proxy.RedisProxyMatcher;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;
import java.util.concurrent.Executor;

@EnableConfigurationProperties(IotRedisProperties.class)
@AutoConfigureBefore(name = "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration")
public class IotRedisConfiguration {

    @Bean(name = "iotRedisTemplate")
    @ConditionalOnMissingBean(name = "iotRedisTemplate")
    public RedisTemplate<String, Object> iotRedisTemplate(RedisConnectionFactory factory) {
        // 创建RedisTemplate<String, Object>对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // 配置连接工厂
        template.setConnectionFactory(factory);

        // FastJsonRedisSerializer
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);

        StringRedisSerializer stringSerial = new StringRedisSerializer();

        // redis key 序列化方式使用stringSerial
        template.setKeySerializer(stringSerial);
        // redis value 序列化方式使用jackson
        template.setValueSerializer(fastJsonRedisSerializer);
        // redis hash key 序列化方式使用stringSerial
        template.setHashKeySerializer(stringSerial);
        // redis hash value 序列化方式使用jackson
        template.setHashValueSerializer(fastJsonRedisSerializer);

        return IotRedis.wrapper.template = template;
    }

    /**
     * 开启消费者任务执行管理器
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "iot.redis", value = "consumer", havingValue = "true")
    public RedisConsumerOperaManager redisConsumerOperaManager(List<RedisConsumerOpera> operas
            , Executor iotTaskExecutor, RedisProperties redisProperties) {
        return new RedisConsumerOperaManager(operas, iotTaskExecutor, redisProperties);
    }

    @Bean
    @ConditionalOnMissingBean(ListConsumerOpera.class)
    @ConditionalOnBean(value = {ListConsumer.class, RedisConsumerOperaManager.class})
    public ListConsumerOpera listConsumerOpera(List<ListConsumer> consumers) {
        return new ListConsumerOpera(consumers);
    }

    @Bean
    @Order(10000)
    @ConditionalOnMissingBean(RedisProxyMatcher.class)
    @ConditionalOnProperty(prefix = "iot.redis", value = "producer", havingValue = "true")
    public RedisProxyMatcher redisProxyMatcher() {
        return new RedisProxyMatcher();
    }

}
