package be.kdg.sa.restaurantservice.api.dtos.order;

import be.kdg.sa.restaurantservice.domain.order.Order;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.UUID;

@ValueObject
public record OrderMessagingDto(UUID id, UUID restaurantId, String comment) {
    public static OrderMessagingDto from(Order order, String reason){
        return new OrderMessagingDto(
                order.getOrderId().id(),
                order.getRestaurantId().id(),
                reason
        );
    }

    public static OrderMessagingDto from(Order order){
        return OrderMessagingDto.from(order, "");
    }
}
