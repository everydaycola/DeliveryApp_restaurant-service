package be.kdg.sa.restaurantservice.api.dtos.restaurant.dish;

import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.UUID;

@ValueObject
public record DishDto(UUID id, String name, String description, DishState state, double price) {
    public static DishDto from(Dish dish){
        return new DishDto(dish.getId().id(), dish.getName(), dish.getDescription(), dish.getState(), dish.getPrice());
    }
}
