package be.kdg.sa.restaurantservice.infrastructure.restaurant;

import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantRepository;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaRestaurantEntity;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaRestaurantRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DbRestaurantRepository implements RestaurantRepository {

    private final JpaRestaurantRepository jpaRestaurantRepository;

    public DbRestaurantRepository(JpaRestaurantRepository jpaRestaurantRepository) {
        this.jpaRestaurantRepository = jpaRestaurantRepository;
    }

    @Override
    public Optional<Restaurant> findById(RestaurantId restaurantId) {
        return this.jpaRestaurantRepository.findById(restaurantId.id())
                .map(JpaRestaurantEntity::toDomain);
    }

    @Override
    public void save(Restaurant restaurant) {
        JpaRestaurantEntity jpaRestaurantEntity = JpaRestaurantEntity.fromDomain(restaurant);
        this.jpaRestaurantRepository.save(jpaRestaurantEntity);
    }

    @Override
    public List<Restaurant> findAll() {
        return this.jpaRestaurantRepository.findAll().stream()
                .map(JpaRestaurantEntity::toDomain)
                .toList();
    }

    public Optional<Restaurant> findByIdWithMenu(RestaurantId restaurantId){
        return this.jpaRestaurantRepository.findByIdWithMenu(restaurantId.id()).map(JpaRestaurantEntity::toDomain);
    }
}
