package com.lgl.mes.rabbitmq.spring.rabbitMQ.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.lgl.mes.rabbitmq.spring.po.Mail;

import java.io.IOException;



@Component
public class TopicListener1 {
	@RabbitListener(queues = "topicqueue1")
	public void displayTopic(Mail mail) throws IOException {
		System.out.println("从topicqueue1取出消息"+mail.toString());
		}
}
