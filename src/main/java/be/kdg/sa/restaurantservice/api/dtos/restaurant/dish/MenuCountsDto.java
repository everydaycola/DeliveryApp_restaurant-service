package be.kdg.sa.restaurantservice.api.dtos.restaurant.dish;

import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishState;

import java.util.List;

public record MenuCountsDto(long publicCount, long notAvailableCount, long notPublicCount) {
    public static MenuCountsDto from(List<Dish> menu){
        return new MenuCountsDto(
                menu.stream().filter(dish -> dish.getState().equals(DishState.PUBLISHED)).count(),
                menu.stream().filter(dish -> dish.getState().equals(DishState.NOT_AVAILABLE)).count(),
                menu.stream().filter(dish -> dish.getState().equals(DishState.NOT_PUBLISHED)).count()
        );
    }
}
