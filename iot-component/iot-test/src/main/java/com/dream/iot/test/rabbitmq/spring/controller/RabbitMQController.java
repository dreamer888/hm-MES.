package com.dream.iot.test.rabbitmq.spring.controller;

import com.dream.iot.test.rabbitmq.spring.po.Mail;
import com.dream.iot.test.rabbitmq.spring.po.TopicMail;
import com.dream.iot.test.rabbitmq.spring.service.Producer;
import com.dream.iot.test.rabbitmq.spring.service.Publisher;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rabbitmq")
public class RabbitMQController {
	@Autowired
	Producer producer;
	
	@Autowired
	Publisher publisher;
	
	@RequestMapping(value="/produce",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public void produce(@ModelAttribute("mail")Mail mail) throws Exception{
		producer.sendMail("myqueue",mail);
	}
	
	@RequestMapping(value="/topic",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public void topic(@ModelAttribute("mail")Mail mail) throws Exception{
		publisher.publishMail(mail);
	}
	
	@RequestMapping(value="/direct",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public void direct(@ModelAttribute("mail")TopicMail mail){
		Mail m=new Mail("",mail.getOrderNo(),mail.getProductId(), mail.getQuality(), (double) 0,0);
		publisher.senddirectMail(m, mail.getRoutingkey());
	}
	
	@RequestMapping(value="/mytopic",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public void topic(@ModelAttribute("mail")TopicMail mail){
		Mail m=new Mail("",mail.getOrderNo(),mail.getProductId(), mail.getQuality(), (double) 0,0);
		publisher.sendtopicMail(m, mail.getRoutingkey());
	}
	
	
	//@RequestMapping("/demo")
	@GetMapping("/demo")
	public String demo(){
		return "rabbitmq/demo";
	}

	@ApiOperation("rabbit admin")
	@GetMapping("/admin")
	public String admin() {

		//return "redirect:http://127.0.0.1:15672/#/";
		return "redirect:http://47.240.54.105:15672/#/";

	}

}
