package be.kdg.sa.restaurantservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @AllArgsConstructor
@ConfigurationProperties(prefix = "spring.rabbitmq.kdg")
public class RabbitMQProperties {
    private final String exchangeName;
    private final String orderPlacedQueue;
    private final String orderPlacedBinding;
    private final String orderRejectedBinding;
    private final String orderAcceptedOrderBinding;
    private final String orderAcceptedDeliveryBinding;
    private final String orderReadyDeliveryBinding;
    private final String orderReadyOrderBinding;
}
