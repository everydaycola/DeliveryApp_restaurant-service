package be.kdg.sa.restaurantservice.api.dtos.restaurant.dish;

import be.kdg.sa.restaurantservice.domain.dish.DishState;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record NewDishDto(String name, String description, DishState state, double price) {

}
