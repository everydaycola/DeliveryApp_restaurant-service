package be.kdg.sa.restaurantservice.infrastructure.rabbitMQ;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    public static final String DEMO_EXCHANGE_NAME = "demo-exchange";

    @Bean
    TopicExchange demoExchange() {
        return new TopicExchange(DEMO_EXCHANGE_NAME);
    }
}
