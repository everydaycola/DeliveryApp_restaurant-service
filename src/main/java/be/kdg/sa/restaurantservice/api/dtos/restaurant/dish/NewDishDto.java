package be.kdg.sa.restaurantservice.api.dtos.restaurant.dish;

import be.kdg.sa.restaurantservice.domain.dish.DishState;

public record NewDishDto(String name, String description, DishState state, double price) {

}
