package com.dream.iot.redis.proxy;

import com.dream.iot.handle.proxy.ProtocolHandleInvocationHandler;
import com.dream.iot.handle.proxy.ProtocolHandleProxy;
import com.dream.iot.handle.proxy.ProtocolHandleProxyMatcher;
import com.dream.iot.redis.ComplexRedisProducer;
import com.dream.iot.redis.SimpleRedisProducer;
import com.dream.iot.redis.producer.RedisProducer;

public class RedisProxyMatcher implements ProtocolHandleProxyMatcher {

    @Override
    public boolean matcher(Object target) {
        return target instanceof RedisProducer;
    }

    @Override
    public ProtocolHandleInvocationHandler invocationHandler(Object target) {
        return new ProtocolHandleInvocationHandler(target) {

            @Override
            protected Class<? extends ProtocolHandleProxy> getProxyClass() {
                return RedisProducer.class;
            }

            @Override
            protected Object proxyHandle(Object value, Object proxy) {
                if(getTarget() instanceof SimpleRedisProducer) {
                    getTarget().persistence(value);
                } else if(getTarget() instanceof ComplexRedisProducer) {

                }

                return value;
            }

            @Override
            public RedisProducer getTarget() {
                return (RedisProducer) super.getTarget();
            }
        };
    }
}
