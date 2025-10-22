package be.kdg.sa.restaurantservice.api.order.dtos;

import be.kdg.sa.restaurantservice.domain.order.Order;

import java.util.UUID;

public record OrderMessagingDto(UUID id, UUID restaurantId) {
    public static OrderMessagingDto from(Order order){
        return new OrderMessagingDto(
                order.getOrderId().id(),
                order.getRestaurantId().id()
        );
    }
}
