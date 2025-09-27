package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.*;
import be.kdg.sa.restaurantservice.domain.restaurant.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurants;

    public RestaurantService(RestaurantRepository restaurants) {
        this.restaurants = restaurants;
    }

    public Restaurant create(OwnerId ownerId,String name){
        final Restaurant restaurant = Restaurant.newInstance( ownerId,name);
        restaurants.save(restaurant);
        return restaurant;
    }

    public Restaurant findById(RestaurantId restaurantId){
        return restaurants.findById(restaurantId).orElseThrow(restaurantId::notFound);
    }

    public List<Restaurant> findAll() {
        return restaurants.findAll();
    }
}
