package be.kdg.sa.common_messaging;

import be.kdg.sa.restaurantservice.api.order.dtos.OrderAcceptedOrRejectedDto;

public record OrderRejectedMessage(OrderAcceptedOrRejectedDto orderDto) {
}
