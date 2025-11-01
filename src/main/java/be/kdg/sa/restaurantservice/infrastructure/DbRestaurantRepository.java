package be.kdg.sa.restaurantservice.infrastructure;

import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.*;
import be.kdg.sa.restaurantservice.infrastructure.jpa.restaurant.JpaDishEntity;
import be.kdg.sa.restaurantservice.infrastructure.jpa.restaurant.JpaRestaurantEntity;
import be.kdg.sa.restaurantservice.infrastructure.jpa.restaurant.JpaRestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class DbRestaurantRepository implements RestaurantRepository {

    private final JpaRestaurantRepository jpaRestaurantRepository;

    public DbRestaurantRepository(JpaRestaurantRepository jpaRestaurantRepository) {
        this.jpaRestaurantRepository = jpaRestaurantRepository;
    }

    @Override
    public Optional<Restaurant> findById(RestaurantId restaurantId) {
        log.info("Finding restaurant {}", restaurantId.id());
        return this.jpaRestaurantRepository.findById(restaurantId.id())
                .map(JpaRestaurantEntity::toDomain);
    }

    @Override
    public void save(Restaurant restaurant) {
        log.info("Saving restaurant {}", restaurant.getId());
        final var jpaRestaurantEntity = JpaRestaurantEntity.fromDomain(restaurant);
        this.jpaRestaurantRepository.save(jpaRestaurantEntity);
    }

    @Override
    public List<Restaurant> findAll() {
        log.info("Finding all restaurants");
        return this.jpaRestaurantRepository.findAll().stream()
                .map(JpaRestaurantEntity::toDomain)
                .toList();
    }

    @Override
    public List<Restaurant> findAllWithOverride() {
        log.info("Finding all restaurants with override");
        return this.jpaRestaurantRepository.findAllByOverrideStatusNot(OverrideStatus.NONE)
                .orElse(List.of())
                .stream()
                .map(JpaRestaurantEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Restaurant> findByIdWithMenu(RestaurantId restaurantId) {
        log.info("Finding restaurant with menu {}", restaurantId.id());
        return this.jpaRestaurantRepository.findByIdWithMenu(restaurantId.id()).map(JpaRestaurantEntity::toDomain);
    }

    @Override
    public Optional<Restaurant> findByIdWithMenuAndOpeningHours(RestaurantId restaurantId) {
        log.info("Finding restaurant with menu and opening hours {}", restaurantId.id());
        return this.jpaRestaurantRepository.findByIdWithMenuAndOpeningHours(restaurantId.id()).map(JpaRestaurantEntity::toDomain);
    }

    @Override
    public Optional<Restaurant> findByOwnerId(OwnerId ownerId) {
        log.info("Finding restaurant for owner {}", ownerId.id());
        return this.jpaRestaurantRepository.findByOwnerId(ownerId.id())
                .map(JpaRestaurantEntity::toDomain);
    }

    @Override public Optional <Dish> findDishById(RestaurantId restaurantId, DishId dishId) {
        log.info("Finding dish {} for restaurant {}", dishId.id(), restaurantId.id());
        return this.jpaRestaurantRepository.findDishById(restaurantId.id(), dishId.id())
                                           .map(JpaDishEntity::toDomain);
    }

    @Override
    public Optional<List<Dish>> findDishesByDishState(RestaurantId restaurantId, DishState state) {
        log.info("Finding dishes with state {} for restaurant {}", state, restaurantId.id());
        return this.jpaRestaurantRepository.findDishesByDishState(restaurantId.id(), state).map(
                jpaDishEntities -> jpaDishEntities.stream().map(JpaDishEntity::toDomain).toList());
    }
}
