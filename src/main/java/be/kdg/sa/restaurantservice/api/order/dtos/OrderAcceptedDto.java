package be.kdg.sa.restaurantservice.api.order.dtos;

import be.kdg.sa.restaurantservice.domain.order.Order;

import java.util.UUID;

public record OrderAcceptedDto(UUID id, UUID restaurantId) {
    public static OrderAcceptedDto from(Order order){
        return new OrderAcceptedDto(
                order.getOrderId().id(),
                order.getRestaurantId().id()
        );
    }
}
