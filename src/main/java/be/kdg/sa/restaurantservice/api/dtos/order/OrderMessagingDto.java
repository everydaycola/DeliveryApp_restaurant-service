package be.kdg.sa.restaurantservice.api.dtos.order;

import be.kdg.sa.restaurantservice.domain.order.Order;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.UUID;

@ValueObject
public record OrderMessagingDto(UUID id, UUID restaurantId) {
    public static OrderMessagingDto from(Order order){
        return new OrderMessagingDto(
                order.getOrderId().id(),
                order.getRestaurantId().id()
        );
    }
}
