package be.kdg.sa.restaurantservice.api.order.dtos;

import be.kdg.sa.restaurantservice.domain.order.Order;

import java.util.UUID;

public record OrderAcceptedOrRejectedDto(UUID id, UUID restaurantId) {
    public static OrderAcceptedOrRejectedDto from(Order order){
        return new OrderAcceptedOrRejectedDto(
                order.getOrderId().id(),
                order.getRestaurantId().id()
        );
    }
}
