package com.lgl.mes.rabbitmq.spring.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//生产者消费者模式的配置,包括一个队列和两个对应的消费者
@Configuration
public class ProducerConsumerConfig {
	
	 	@Bean
	    public Queue myQueue() {
	       Queue queue=new Queue("myqueue");
	       return queue;
	    }
	    
}
