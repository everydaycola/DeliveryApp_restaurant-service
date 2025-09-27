package be.kdg.sa.restaurantservice.domain.restaurant;

import be.kdg.sa.restaurantservice.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;


public record RestaurantId(UUID id) {
    public RestaurantId {
        Assert.notNull(id, "id cannot be null");
    }

    public NotFoundException notFound() {
        return new NotFoundException("Restaurant [" + id + "] not found");
    }

    public static RestaurantId create() {
        return new RestaurantId(UUID.randomUUID());
    }
}
