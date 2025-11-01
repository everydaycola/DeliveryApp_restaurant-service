package be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages;

import be.kdg.sa.restaurantservice.api.dtos.order.OrderMessagingDto;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record OrderAcceptedMessage(OrderMessagingDto orderDto) {
}
