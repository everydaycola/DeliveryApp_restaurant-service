package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.api.dtos.AddressDto;
import be.kdg.sa.restaurantservice.api.dtos.RestaurantOpeningHoursDto;
import be.kdg.sa.restaurantservice.config.DomainProperties;
import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.*;
import be.kdg.sa.restaurantservice.domain.restaurant.priceCriteria.MeanPriceCriteriaCalculator;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Timer;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurants;
    private final DomainProperties domainProperties;

    public RestaurantService(RestaurantRepository restaurants, DomainProperties domainProperties) {
        this.restaurants = restaurants;
        this.domainProperties = domainProperties;
    }

    //Create
    //Restaurant
    public Restaurant create(OwnerId ownerId, String name, AddressDto addressDto, String contactEmail, RestaurantType type, List<RestaurantOpeningHoursDto> openingHoursDtos, String logo) {
        //Convert AddressDto -> Address
        Address address = new Address(addressDto.street(), addressDto.number(), addressDto.postalCode(), addressDto.country());

        //Create Restaurant
        final Restaurant restaurant = Restaurant.newInstance(ownerId, name, address, contactEmail, type, logo, new MeanPriceCriteriaCalculator());

        //Convert RestaurantOpeningHoursDto -> RestaurantOpeningHours
        openingHoursDtos.forEach(rohDto -> restaurant.addOpeningHours(rohDto.day(),rohDto.openingTime(), rohDto.closingTime()));

        restaurants.save(restaurant);

        return restaurant;
    }

    //Dish
    public Dish createDish(RestaurantId id, String dishName, String description, double price){
        Restaurant restaurant = restaurants.findByIdWithMenu(id).orElseThrow(id::notFound);
        Dish dish = restaurant.addDish(dishName, description, price);
        restaurants.save(restaurant);
        return dish;
    }

    //Find
    //Restaurant
    public Restaurant findById(RestaurantId restaurantId) {
        Restaurant restaurant = restaurants.findById(restaurantId).orElseThrow(restaurantId::notFound);
        if (!restaurant.isOverwriteOpeningHours()) checkIfOpenAndUpdate(restaurant);
        return restaurant;
    }

    public Restaurant findByIdWithMenu(RestaurantId restaurantId){
        Restaurant restaurant = restaurants.findByIdWithMenu(restaurantId).orElseThrow(restaurantId::notFound);
        if (!restaurant.isOverwriteOpeningHours()) checkIfOpenAndUpdate(restaurant);
        return restaurant;
    }

    public Restaurant findByIdWithMenuAndOpeningHours(RestaurantId restaurantId){
        Restaurant restaurant = restaurants.findByIdWithMenuAndOpeningHours(restaurantId).orElseThrow(restaurantId::notFound);
        if (!restaurant.isOverwriteOpeningHours()) checkIfOpenAndUpdate(restaurant);
        return restaurant;
    }

    public List<Restaurant> findAll() {
        List<Restaurant> restos = restaurants.findAll();
        restos.forEach(restaurant -> {
            if (!restaurant.isOverwriteOpeningHours()) checkIfOpenAndUpdate(restaurant);
        });
        return restos;
    }

    //Dish
    public List<Dish> findMenuWithDishState(RestaurantId restaurantId, DishState state){
        return restaurants.findDishesByDishState(restaurantId, state).orElseThrow(restaurantId::notFound);
    }

    //Update
    //Restaurant
    private void checkIfOpenAndUpdate(Restaurant restaurant){
        restaurant.checkIfOpen();
        restaurants.save(restaurant);
    }

    public Restaurant openOrCloseRestaurant(RestaurantId restaurantId, boolean isOpen, OwnerId ownerId){
        Restaurant restaurant =  findById(restaurantId);
        restaurant.checkIfOwnerBy(ownerId);
        restaurant.open(isOpen);
        restaurants.save(restaurant);
        return restaurant;
    }

    public void resetOverwrite(RestaurantId restaurantId, OwnerId ownerId){
        Restaurant restaurant = findById(restaurantId);
        restaurant.checkIfOwnerBy(ownerId);
        restaurant.stopOverwriteOpeningHours();
    }

    //Dish
    public Dish findDishById(RestaurantId restaurantId, DishId dishId) {
        return restaurants.findDishById(restaurantId, dishId).orElseThrow(dishId::notFound);
    }

    public Dish updateDish(RestaurantId restaurantId, DishId dishId, String name, String description, OwnerId ownerId){
        Restaurant restaurant = findByIdWithMenu(restaurantId);
        restaurant.checkIfOwnerBy(ownerId);
        Dish dish = restaurant.updateDish(dishId, name, description);
        restaurants.save(restaurant);

        return dish;
    }

    public Dish updateDishState(RestaurantId restaurantId, DishId dishId, DishState state, OwnerId ownerId){
       Restaurant restaurant = findByIdWithMenu(restaurantId);
       restaurant.checkIfOwnerBy(ownerId);
       Dish dish = restaurant.updateDishState(dishId, state, domainProperties.getMaxDishes());

       restaurants.save(restaurant);

       return dish;
    }

    public List<Dish> publishReadyDishes(RestaurantId restaurantId, OwnerId ownerId){
        Restaurant restaurant = findByIdWithMenu(restaurantId);
        restaurant.checkIfOwnerBy(ownerId);
        List<Dish> readyDishes = findMenuWithDishState(restaurantId,DishState.READY_FOR_PUBLISHING);

        List<Dish> newPublicDished = readyDishes.stream()
                .map(dish -> restaurant.updateDishState(dish.getId(),DishState.PUBLISHED, domainProperties.getMaxDishes())).toList();

        restaurants.save(restaurant);

        return newPublicDished;
    }

    public List<Dish> publishDishesOnSchedule(RestaurantId id , Date scheduledDate, List<DishId> dishIds, OwnerId ownerId){
        Restaurant restaurant = findByIdWithMenu(id);
        restaurant.checkIfOwnerBy(ownerId);

        //restaurant is saved in PublishDishesTask to the repository
        new Timer().schedule(new PublishDishesTask(restaurants, domainProperties.getMaxDishes(), restaurant, dishIds), scheduledDate);

        return dishIds.stream().map(restaurant::getDish).toList();
    }

    public void checkOwnership(RestaurantId restaurantId, OwnerId ownerId) {
        final Restaurant restaurant = restaurants.findById(restaurantId).orElseThrow(restaurantId::notFound);
        restaurant.checkIfOwnerBy(ownerId);
    }
}
