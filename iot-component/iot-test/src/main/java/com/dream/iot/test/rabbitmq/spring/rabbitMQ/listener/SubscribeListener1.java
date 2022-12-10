package com.dream.iot.test.rabbitmq.spring.rabbitMQ.listener;

import com.dream.iot.test.rabbitmq.spring.po.Mail;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;



@Component
public class SubscribeListener1 {
	@RabbitListener(queues = "queue1")
	public void subscribe(Mail mail) throws IOException {
		System.out.println("订阅者1收到消息"+mail.toString());
	}
}
