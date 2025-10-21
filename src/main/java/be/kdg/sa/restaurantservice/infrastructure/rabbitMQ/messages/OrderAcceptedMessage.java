package be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages;

import be.kdg.sa.restaurantservice.api.order.dtos.OrderAcceptedDto;

public record OrderAcceptedMessage(OrderAcceptedDto orderDto) {
}
