package be.kdg.sa.restaurantservice.domain.order;

import lombok.extern.slf4j.Slf4j;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.UUID;

@Slf4j
@ValueObject
public record OrderLineId(UUID id) {
    public static OrderLineId create() {
        return new OrderLineId(UUID.randomUUID());
    }
}
