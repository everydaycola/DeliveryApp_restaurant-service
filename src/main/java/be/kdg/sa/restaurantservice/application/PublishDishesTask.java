package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantRepository;

import java.util.List;
import java.util.TimerTask;

public class PublishDishesTask extends TimerTask {
    private RestaurantRepository restaurants;
    private Restaurant restaurant;
    private List<DishId> dishIds;

    public PublishDishesTask(RestaurantRepository restaurants, Restaurant restaurant, List<DishId> dishIds) {
        this.restaurants = restaurants;
        this.restaurant = restaurant;
        this.dishIds = dishIds;
    }

    @Override
    public void run() {
        dishIds.forEach(dishId -> restaurant.updateDishState(dishId, DishState.PUBLISHED));
        restaurants.save(restaurant);
    }
}
