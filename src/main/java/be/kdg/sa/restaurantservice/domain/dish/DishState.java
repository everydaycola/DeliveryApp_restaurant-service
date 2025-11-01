package be.kdg.sa.restaurantservice.domain.dish;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public enum DishState {
    PUBLISHED,
    NOT_AVAILABLE,
    NOT_PUBLISHED,
    READY_FOR_PUBLISHING
}
