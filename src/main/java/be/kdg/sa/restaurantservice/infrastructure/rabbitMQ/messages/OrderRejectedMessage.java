package be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages;

import be.kdg.sa.restaurantservice.api.order.dtos.OrderAcceptedOrRejectedDto;

public record OrderRejectedMessage(OrderAcceptedOrRejectedDto orderDto) {
}
