package be.kdg.sa.restaurantservice.infrastructure.rabbitMQ;

import be.kdg.sa.restaurantservice.config.RabbitMQProperties;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    private final RabbitMQProperties properties;

    public RabbitMQTopology(RabbitMQProperties properties) {
        this.properties = properties;
    }

    @Bean
    TopicExchange kdgExchange() {
        return new TopicExchange(properties.getExchangeName());
    }

    @Bean
    Queue orderPlacedQueue() {
        return QueueBuilder.nonDurable(properties.getOrderPlacedQueue()).build();
    }

    @Bean
    Binding orderPlacedBinding(){
        return BindingBuilder.bind(orderPlacedQueue()).to(kdgExchange()).with(properties.getOrderPlacedBinding());
    }

}
