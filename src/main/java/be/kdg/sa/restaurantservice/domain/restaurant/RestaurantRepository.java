package be.kdg.sa.restaurantservice.domain.restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findById(RestaurantId restaurantId);
    void save(Restaurant restaurant);
    List<Restaurant> findAll();
}
