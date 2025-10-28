package be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages;

import be.kdg.sa.restaurantservice.api.dtos.order.OrderDto;

public record OrderPlacedMessage(OrderDto orderDto) {
}
