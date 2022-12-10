package com.lgl.mes.rabbitmq.spring.rabbitMQ.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.lgl.mes.rabbitmq.spring.po.Mail;

import java.io.IOException;



@Component
public class SubscribeListener1 {
	@RabbitListener(queues = "queue1")
	public void subscribe(Mail mail) throws IOException {
		System.out.println("订阅者1收到消息"+mail.toString());
	}
}
