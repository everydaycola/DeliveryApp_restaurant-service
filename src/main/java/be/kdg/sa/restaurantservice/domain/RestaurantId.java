package be.kdg.sa.restaurantservice.domain;

import org.springframework.util.Assert;

import java.util.UUID;


public record RestaurantId(UUID id) {
    public RestaurantId {
        Assert.notNull(id, "id cannot be null");
    }

    public static RestaurantId create() {
        return new RestaurantId(UUID.randomUUID());
    }
}
