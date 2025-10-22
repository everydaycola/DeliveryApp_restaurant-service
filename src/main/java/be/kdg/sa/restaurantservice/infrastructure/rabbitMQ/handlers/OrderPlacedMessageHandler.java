package be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.handlers;

import be.kdg.sa.restaurantservice.application.OrderService;
import be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.RabbitMQTopology;
import be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages.OrderPlacedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderPlacedMessageHandler {
    OrderService orderService;

    public OrderPlacedMessageHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitMQTopology.RESTAURANT_QUEUE_NAME)
    void onOrderPlacedMessageReceived(OrderPlacedMessage message) {
        log.info("Order Placed Message Received: Order={}", message.orderDto().orderId());

        orderService.placeOrder(message);
    }
}
