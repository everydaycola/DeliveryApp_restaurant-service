package be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.handlers;


import be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.RabbitMQTopology;
import be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages.HelloMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HelloMessageHandler {

    @RabbitListener(queues = RabbitMQTopology.ORDER_QUEUE_NAME)
    void onSomethingMessageReceived(HelloMessage message) {
        log.info("Message Received: {}", message);
    }
}
