package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantRepository;

import java.util.List;
import java.util.TimerTask;

//TODO: Do with spring @Schedule
public class PublishDishesTask extends TimerTask {
    private final RestaurantRepository restaurants;
    private final Restaurant restaurant;
    private final List<DishId> dishIds;
    private final int maxDishes;

    public PublishDishesTask(RestaurantRepository restaurants, int maxDishes, Restaurant restaurant, List<DishId> dishIds) {
        this.restaurants = restaurants;
        this.restaurant = restaurant;
        this.dishIds = dishIds;
        this.maxDishes = maxDishes;
    }

    @Override
    public void run() {
        dishIds.forEach(dishId -> restaurant.updateDishState(dishId, DishState.PUBLISHED, maxDishes));
        restaurants.save(restaurant);
    }
}
