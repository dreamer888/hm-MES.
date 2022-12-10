package com.dream.iot.test.rabbitmq.spring.service.impl;

import com.dream.iot.test.rabbitmq.spring.po.Mail;
import com.dream.iot.test.rabbitmq.spring.service.Producer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("producer")
public class ProducerImpl implements Producer{
	@Autowired
	RabbitTemplate rabbitTemplate;
	public void sendMail(String queue,Mail mail) {
		//rabbitTemplate.setQueue(queue); //deprecated already
		rabbitTemplate.setDefaultReceiveQueue(queue);
		rabbitTemplate.convertAndSend(queue,mail);
	}

}
