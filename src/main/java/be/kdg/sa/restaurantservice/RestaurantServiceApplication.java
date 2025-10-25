package be.kdg.sa.restaurantservice;

import be.kdg.sa.restaurantservice.config.DomainProperties;
import be.kdg.sa.restaurantservice.config.RabbitMQProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({DomainProperties.class, RabbitMQProperties.class})
public class RestaurantServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestaurantServiceApplication.class, args);
	}
}
