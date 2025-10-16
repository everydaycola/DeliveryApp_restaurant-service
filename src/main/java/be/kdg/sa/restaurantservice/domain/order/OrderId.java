package be.kdg.sa.restaurantservice.domain.order;

import be.kdg.sa.restaurantservice.domain.NotFoundException;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.UUID;

@ValueObject
public record OrderId(UUID id) {
    public static OrderId create() {
        return new OrderId(UUID.randomUUID());
    }

    public NotFoundException notFound() {
        return new NotFoundException("Order [" + id + "] not found");
    }
}
