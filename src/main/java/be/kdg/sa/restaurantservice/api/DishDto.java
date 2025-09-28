package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishState;

import java.util.UUID;

public record DishDto(UUID id, String name, String description, DishState state) {
    public static DishDto from(Dish dish){
        return new DishDto(dish.getId().id(), dish.getName(), dish.getDescription(), dish.getState());
    }
}
