package be.kdg.sa.restaurantservice.domain.dish;

import be.kdg.sa.restaurantservice.domain.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jmolecules.ddd.annotation.Entity;
import org.springframework.util.Assert;

import java.util.UUID;

@Entity
@Slf4j
public record DishId(UUID id) {
    public DishId {
        Assert.notNull(id, "id cannot be null");
    }

    public NotFoundException notFound() {
        log.error("Dish with id {} not found", id);
        return new NotFoundException("Dish [" + id + "] not found");
    }

    public static DishId create() {
        return new DishId(UUID.randomUUID());
    }
}
