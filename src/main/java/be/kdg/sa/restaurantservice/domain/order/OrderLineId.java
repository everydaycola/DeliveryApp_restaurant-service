package be.kdg.sa.restaurantservice.domain.order;

import be.kdg.sa.restaurantservice.domain.NotFoundException;

import java.util.UUID;

public record OrderLineId(UUID id) {
    public static OrderLineId create() {
        return new OrderLineId(UUID.randomUUID());
    }

    public NotFoundException notFound() {
        return new NotFoundException("OrderLine [" + id + "] not found");
    }
}
