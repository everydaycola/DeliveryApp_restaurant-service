package be.kdg.sa.restaurantservice.domain.restaurant;

import be.kdg.sa.restaurantservice.domain.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.util.Assert;

import java.util.UUID;

@Slf4j
@ValueObject
public record OwnerId(UUID id) {
    public OwnerId {
        Assert.notNull(id, "Id cannot be null");
    }

    public NotFoundException restaurantNotFound() {
        log.error("Restaurant for owner {} not found", id);
        return new NotFoundException("Restaurant for owner [" + id + "] not found");
    }
}
