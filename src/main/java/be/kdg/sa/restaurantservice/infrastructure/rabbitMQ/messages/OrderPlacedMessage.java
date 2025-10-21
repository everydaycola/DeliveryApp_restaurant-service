package be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages;

import be.kdg.sa.restaurantservice.api.order.dtos.OrderDto;

public record OrderPlacedMessage(OrderDto orderDto) {
}
