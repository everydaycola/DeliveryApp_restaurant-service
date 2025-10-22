package be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages;

import be.kdg.sa.restaurantservice.api.order.dtos.OrderMessagingDto;

public record OrderReadyMessage(OrderMessagingDto orderDto) {
}
