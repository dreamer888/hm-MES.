package com.dream.iot.test.rabbitmq.spring.service;

import com.dream.iot.test.rabbitmq.spring.po.Mail;

public interface Producer {
	public void sendMail(String queue,Mail mail);//向队列queue发送消息
}
