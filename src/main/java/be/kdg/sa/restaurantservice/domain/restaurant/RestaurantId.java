package be.kdg.sa.restaurantservice.domain.restaurant;

import be.kdg.sa.restaurantservice.domain.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.util.Assert;

import java.util.UUID;

@Slf4j
@ValueObject
public record RestaurantId(UUID id) {
    public RestaurantId {
        Assert.notNull(id, "id cannot be null");
    }

    public NotFoundException notFound() {
        log.error("Restaurant with id {} not found", id);
        return new NotFoundException("Restaurant [" + id + "] not found");
    }

    public static RestaurantId create() {
        return new RestaurantId(UUID.randomUUID());
    }
}
