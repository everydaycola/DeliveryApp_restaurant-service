package be.kdg.sa.restaurantservice.domain.restaurant;

import be.kdg.sa.restaurantservice.domain.NotFoundException;
import org.springframework.util.Assert;

import java.util.UUID;

public record OwnerId(UUID id) {
    public OwnerId {
        Assert.notNull(id, "Id cannot be null");
    }

    public NotFoundException restaurantNotFound() {
        return new NotFoundException("Restaurant for owner [" + id + "] not found");
    }
}
