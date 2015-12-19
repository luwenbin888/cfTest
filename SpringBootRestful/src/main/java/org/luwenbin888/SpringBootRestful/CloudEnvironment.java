package org.luwenbin888.SpringBootRestful;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.AmqpServiceInfo;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value=1)
public class CloudEnvironment implements CommandLineRunner {
	private static String rabbitMQUri;
	private static boolean isCloud;

	@Override
	public void run(String... arg0) throws Exception {
		try {
			CloudFactory cloudFactory = new CloudFactory();
			Cloud cloud = cloudFactory.getCloud();
			List<ServiceInfo> cloudServices = cloud.getServiceInfos();
			for (ServiceInfo serviceInfo : cloudServices) {
				if (serviceInfo instanceof AmqpServiceInfo) {
					AmqpServiceInfo rabbitMQServiceInfo = (AmqpServiceInfo)serviceInfo;
					rabbitMQUri = rabbitMQServiceInfo.getUri();
					System.out.println("Rabbit MQ service Uri: " + rabbitMQUri);
				}
			}
			isCloud = true;
		} catch (CloudException ex) {
			System.out.println("Not run in cloud");
		}
	}
	
	public static boolean isCloudEnvironment() {
		return isCloud;
	}
	
	public static String getRabbitMQUri() {
		return rabbitMQUri;
	}
}
