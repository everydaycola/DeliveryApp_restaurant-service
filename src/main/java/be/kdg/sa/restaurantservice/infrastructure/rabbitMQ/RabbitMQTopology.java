package be.kdg.sa.restaurantservice.infrastructure.rabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    public static final String KDG_EXCHANGE_NAME = "kdg_exchange";

    public static final String RESTAURANT_QUEUE_NAME = "restaurant_queue";

    @Bean
    TopicExchange kdgExchange() {
        return new TopicExchange(KDG_EXCHANGE_NAME);
    }

    @Bean
    Queue restaurantQueue() {
        return QueueBuilder.nonDurable(RESTAURANT_QUEUE_NAME).build();
    }

    @Bean
    Binding restaurantQueueBinding(TopicExchange kdgExchange) {
        return BindingBuilder.bind(restaurantQueue()).to(kdgExchange).with("order.*");
    }
}
