package be.kdg.sa.restaurantservice.domain.restaurant;

import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findById(RestaurantId restaurantId);
    Optional<Restaurant> findByIdWithMenu(RestaurantId restaurantId);
    Optional<Restaurant> findByIdWithMenuAndOpeningHours(RestaurantId restaurantId);
    Optional<Restaurant> findByOwnerId(OwnerId ownerId);
    List<Restaurant> findAll();
    List<Restaurant> findAllWithOverride();
    Optional<Dish> findDishById(RestaurantId restaurantId, DishId dishId);
    Optional<List<Dish>> findDishesByDishState(RestaurantId restaurantId, DishState state);
    void save(Restaurant restaurant);
}
