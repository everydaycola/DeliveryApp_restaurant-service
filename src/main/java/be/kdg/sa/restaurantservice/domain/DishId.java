package be.kdg.sa.restaurantservice.domain;

import org.springframework.util.Assert;

import java.util.UUID;

public record DishId(UUID id) {
    public DishId {
        Assert.notNull(id, "id cannot be null");
    }

    public static DishId create() {
        return new DishId(UUID.randomUUID());
    }
}
