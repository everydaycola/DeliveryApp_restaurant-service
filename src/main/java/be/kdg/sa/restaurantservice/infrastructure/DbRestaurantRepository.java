package be.kdg.sa.restaurantservice.infrastructure;

import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantRepository;
import be.kdg.sa.restaurantservice.infrastructure.jpa.JpaDishEntity;
import be.kdg.sa.restaurantservice.infrastructure.jpa.JpaRestaurantEntity;
import be.kdg.sa.restaurantservice.infrastructure.jpa.JpaRestaurantRepository;
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

    @Override
    public Optional<Restaurant> findByIdWithMenu(RestaurantId restaurantId) {
        return this.jpaRestaurantRepository.findByIdWithMenu(restaurantId.id()).map(JpaRestaurantEntity::toDomain);
    }

    @Override
    public Optional<Restaurant> findByIdWithMenuAndOpeningHours(RestaurantId restaurantId) {
        return this.jpaRestaurantRepository.findByIdWithMenuAndOpeningHours(restaurantId.id()).map(JpaRestaurantEntity::toDomain);
    }

    @Override public Optional <Dish> findDishById(RestaurantId restaurantId, DishId dishId) {
        return this.jpaRestaurantRepository.findDishById(restaurantId.id(), dishId.id())
                                           .map(JpaDishEntity::toDomain);
    }

    @Override
    public Optional<List<Dish>> findDishesByDishState(RestaurantId restaurantId, DishState state) {
        return this.jpaRestaurantRepository.findDishesByDishState(restaurantId.id(), state).map(
                jpaDishEntities -> jpaDishEntities.stream().map(JpaDishEntity::toDomain).toList());
    }
}
