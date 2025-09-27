package be.kdg.sa.restaurantservice.domain;

import org.springframework.util.Assert;

import java.util.UUID;

public record OwnerId(UUID id) {
    public OwnerId {
        Assert.notNull(id, "Id cannot be null");
    }
}
