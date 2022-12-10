package com.dream.iot.test.rabbitmq.spring.rabbitMQ.listener;


import com.dream.iot.test.db.product.entity.SpProduct;
import com.dream.iot.test.db.product.service.ISpProductService;
import com.dream.iot.test.rabbitmq.spring.po.Mail;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class QueueListener1 {
	@Autowired
	private ISpProductService iSpProductService;
	
	@RabbitListener(queues = "myqueue")
	public void displayMail(Mail mail) throws Exception {
		System.out.println("队列监听器1号收到消息"+mail.toString());
		SpProduct record = new SpProduct();
		BeanUtils.copyProperties(mail, record);
		iSpProductService.AddProdut(record);
	}
}
