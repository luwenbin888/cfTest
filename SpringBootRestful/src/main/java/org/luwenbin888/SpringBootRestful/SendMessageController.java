package org.luwenbin888.SpringBootRestful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendMessageController {
	
	@Autowired
	RabbitMQService rabbitMqService;
	
	@RequestMapping("/sendMsg/send")
	public void sendMessage(String message) {
		rabbitMqService.sendMessage(message);
	}
}
