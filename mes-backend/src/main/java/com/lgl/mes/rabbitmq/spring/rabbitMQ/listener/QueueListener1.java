package com.lgl.mes.rabbitmq.spring.rabbitMQ.listener;


import com.lgl.mes.product.entity.SpProduct;
import com.lgl.mes.product.service.ISpProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.lgl.mes.rabbitmq.spring.po.Mail;


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
