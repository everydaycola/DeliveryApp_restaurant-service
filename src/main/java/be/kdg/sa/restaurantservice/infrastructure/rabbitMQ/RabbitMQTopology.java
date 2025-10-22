package be.kdg.sa.restaurantservice.infrastructure.rabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    public static final String KDG_EXCHANGE_NAME = "kdg_exchange";

    public static final String ORDER_PLACED_QUEUE_NAME = "order_placed";

    @Bean
    TopicExchange kdgExchange() {
        return new TopicExchange(KDG_EXCHANGE_NAME);
    }

    @Bean
    Queue orderPlacedQueue() {
        return QueueBuilder.nonDurable(ORDER_PLACED_QUEUE_NAME).build();
    }

    @Bean
    Binding orderPlacedBinding(){
        return BindingBuilder.bind(orderPlacedQueue()).to(kdgExchange()).with("order.placed");
    }

}
