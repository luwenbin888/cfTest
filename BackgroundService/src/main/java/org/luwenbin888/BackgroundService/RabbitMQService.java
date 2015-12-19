package org.luwenbin888.BackgroundService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@Component
public class RabbitMQService implements CommandLineRunner {
	
	private static String queueName = "test-queue";

	@Override
	public void run(String... arg0) throws Exception {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.print("Starting rabbit mq service");
				ConnectionFactory factory = new ConnectionFactory();
				try {
					factory.setUri(CloudEnvironment.getRabbitMQUri());
					Connection connection = factory.newConnection();
					final Channel channel = connection.createChannel();
					channel.queueDeclare(queueName, true, false, false, null);
					
					boolean autoAck = false;
					channel.basicConsume(queueName, autoAck, "",
						     new DefaultConsumer(channel) {
						         @Override
						         public void handleDelivery(String consumerTag,
						                                    Envelope envelope,
						                                    AMQP.BasicProperties properties,
						                                    byte[] body)
						             throws IOException
						         {
						        	 System.out.println("Received message " +  new String(body));
						             long deliveryTag = envelope.getDeliveryTag();
						             channel.basicAck(deliveryTag, false);
						         }
						     });
				} catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

}
