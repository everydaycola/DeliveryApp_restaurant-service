package be.kdg.sa.restaurantservice.api.dtos.restaurant.dish;

import be.kdg.sa.restaurantservice.domain.dish.DishState;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record UpdateDishStateDto(DishState state) {
}
