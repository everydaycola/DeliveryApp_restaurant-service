package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.api.dtos.restaurant.NewRestaurantDto;
import be.kdg.sa.restaurantservice.config.DomainProperties;
import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.OwnerId;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class RestaurantService {
    private final RestaurantRepository restaurants;
    private final DomainProperties domainProperties;
    private final RestaurantTaskScheduler restaurantTaskScheduler;

    public RestaurantService(RestaurantRepository restaurants, DomainProperties domainProperties, RestaurantTaskScheduler publishDishesScheduler) {
        this.restaurants = restaurants;
        this.domainProperties = domainProperties;
        this.restaurantTaskScheduler = publishDishesScheduler;
    }

    //Create
    //Restaurant
    public Restaurant create(
            OwnerId ownerId,
            NewRestaurantDto newRestaurantDto
    ) {
        log.info("Creating restaurant for owner {}", ownerId.id());
        return (Restaurant) restaurants.findByOwnerId(ownerId)
                .map(found -> {
                    log.error("Owner {} already has a restaurant: {} ({})", ownerId.id(), found.getName(), found.getId());
                    throw new IllegalStateException(String.format("Owner (%s) already has a restaurant: %s (%s)", ownerId.id(), found.getName(), found.getId()));
                })
                .orElseGet(() -> {
                    final Restaurant newRestaurant = newRestaurantDto.toRestaurant(ownerId);
                    restaurants.save(newRestaurant);
                    return newRestaurant;
                });
    }

    //Dish
    public Dish createDish(RestaurantId id, String dishName, String description, double price){
        log.info("Creating dish for restaurant {}", id.id());
        final var restaurant = restaurants.findByIdWithMenu(id).orElseThrow(id::notFound);
        final var dish = restaurant.addDish(dishName, description, price);
        restaurants.save(restaurant);
        return dish;
    }

    //Find
    //Restaurant
    public Restaurant findById(RestaurantId restaurantId) {
        log.info("Finding restaurant {}", restaurantId.id());
        return restaurants.findById(restaurantId).orElseThrow(restaurantId::notFound);
    }

    public Restaurant findByOwnerId(OwnerId ownerId) {
        log.info("Finding restaurant for owner {}", ownerId.id());
        return restaurants.findByOwnerId(ownerId).orElseThrow(ownerId::restaurantNotFound);
    }

    public Restaurant findByIdWithMenu(RestaurantId restaurantId) {
        log.info("Finding restaurant with menu {}", restaurantId.id());
        return restaurants.findByIdWithMenu(restaurantId).orElseThrow(restaurantId::notFound);
    }

    public Restaurant findByIdWithMenuAndOpeningHours(RestaurantId restaurantId) {
        log.info("Finding restaurant with menu and opening hours {}", restaurantId.id());
        return restaurants.findByIdWithMenuAndOpeningHours(restaurantId).orElseThrow(restaurantId::notFound);
    }

    public List<Restaurant> findAll() {
        log.info("Finding all restaurants");
        return restaurants.findAll();
    }

    //Dish
    public List<Dish> findMenuWithDishState(RestaurantId restaurantId, DishState state) {
        log.info("Finding dishes with state {} for restaurant {}", state, restaurantId.id());
        return restaurants.findDishesByDishState(restaurantId, state).orElseThrow(restaurantId::notFound);
    }

    //Update
    //Restaurant
    public Restaurant openOrCloseRestaurant(RestaurantId restaurantId, boolean isOpen, OwnerId ownerId) {
        log.info("Updating restaurant {} to {}", restaurantId.id(), isOpen);
        final var restaurant = findById(restaurantId);
        restaurant.checkIfOwnerBy(ownerId);
        restaurant.open(isOpen);
        restaurants.save(restaurant);
        return restaurant;
    }

    public Restaurant resetOverwrite(RestaurantId restaurantId, OwnerId ownerId) {
        log.info("Resetting overwrite for restaurant {}", restaurantId.id());
        final var restaurant = findById(restaurantId);
        restaurant.checkIfOwnerBy(ownerId);
        restaurant.stopOverwriteOpeningHours();
        return restaurant;
    }

    //Dish
    public Dish findDishById(RestaurantId restaurantId, DishId dishId) {
        log.info("Finding dish {} for restaurant {}", dishId.id(), restaurantId.id());
        return restaurants.findDishById(restaurantId, dishId).orElseThrow(dishId::notFound);
    }

    public Dish updateDish(RestaurantId restaurantId, DishId dishId, String name, String description, OwnerId ownerId) {
        log.info("Updating dish {} for restaurant {}", dishId.id(), restaurantId.id());
        final var restaurant = findByIdWithMenu(restaurantId);
        restaurant.checkIfOwnerBy(ownerId);
        final var dish = restaurant.updateDish(dishId, name, description);
        restaurants.save(restaurant);
        return dish;
    }

    public Dish updateDishState(RestaurantId restaurantId, DishId dishId, DishState state, OwnerId ownerId) {
        log.info("Updating dish {} for restaurant {} to {}", dishId.id(), restaurantId.id(), state);
        final var restaurant = findByIdWithMenu(restaurantId);
        restaurant.checkIfOwnerBy(ownerId);
        final var dish = restaurant.updateDishState(dishId, state, domainProperties.getMaxDishes());
        restaurants.save(restaurant);
        return dish;
    }

    public List<Dish> publishReadyDishes(RestaurantId restaurantId, OwnerId ownerId) {
        log.info("Publishing ready dishes for restaurant {}", restaurantId.id());
        final var restaurant = findByIdWithMenu(restaurantId);
        restaurant.checkIfOwnerBy(ownerId);
        final var readyDishes = findMenuWithDishState(restaurantId, DishState.READY_FOR_PUBLISHING);

        final var newPublicDished = readyDishes.stream()
                .map(dish -> restaurant.updateDishState(dish.getId(), DishState.PUBLISHED, domainProperties.getMaxDishes())).toList();

        restaurants.save(restaurant);

        return newPublicDished;
    }

    public List<Dish> publishDishesOnSchedule(RestaurantId id, Date scheduledDate, List<DishId> dishIds, OwnerId ownerId) {
        log.info("Publishing dishes on schedule for restaurant {}", id.id());
        final var restaurant = findByIdWithMenu(id);
        restaurant.checkIfOwnerBy(ownerId);

        restaurantTaskScheduler.ScheduleDishPublishing(scheduledDate, dishIds, restaurant, domainProperties.getMaxDishes());

        return dishIds.stream().map(restaurant::getDish).toList();
    }

    public void checkOwnership(RestaurantId restaurantId, OwnerId ownerId) {
        log.info("Checking if restaurant {} is owned by {}", restaurantId.id(), ownerId.id());
        final var restaurant = restaurants.findById(restaurantId).orElseThrow(restaurantId::notFound);
        restaurant.checkIfOwnerBy(ownerId);
    }
}
