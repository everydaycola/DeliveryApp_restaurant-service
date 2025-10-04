package be.kdg.sa.restaurantservice.infrastructure;

import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryRestaurantRepository implements RestaurantRepository {
    private final Map<RestaurantId, Restaurant> restaurants = new HashMap<>();

    @Override
    public Optional<Restaurant> findById(RestaurantId restaurantId) {
        return Optional.ofNullable(restaurants.get(restaurantId));
    }

    @Override
    public void save(Restaurant restaurant) {
        restaurants.put(restaurant.getId(), restaurant);
    }

    @Override
    public List<Restaurant> findAll() {
        return List.copyOf(restaurants.values());
    }

    @Override
    public Optional<Restaurant> findByIdWithMenu(RestaurantId restaurantId) {
        return Optional.empty();
    }
}
