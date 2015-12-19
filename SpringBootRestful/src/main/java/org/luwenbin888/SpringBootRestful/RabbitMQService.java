package org.luwenbin888.SpringBootRestful;

import java.io.IOException;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Service
public class RabbitMQService {

	private static Channel channel;
	private static final String queueName = "test-queue";

	private void initChannel() {

		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri(CloudEnvironment.getRabbitMQUri());
			Connection conn = factory.newConnection();
			channel = conn.createChannel();
			channel.queueDeclare(queueName, true, false, false, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void sendMessage(String message) {
		if (channel == null) {
			initChannel();
		}

		try {
			channel.basicPublish("", "", null, message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
