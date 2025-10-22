package be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages;

import be.kdg.sa.restaurantservice.api.dtos.OrderMessagingDto;

public record OrderRejectedMessage(OrderMessagingDto orderDto) {
}
