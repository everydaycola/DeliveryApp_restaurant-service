package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.api.restaurant.dtos.AddressDto;
import be.kdg.sa.restaurantservice.api.restaurant.dtos.RestaurantOpeningHoursDto;
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

    public RestaurantService(RestaurantRepository restaurants) {
        this.restaurants = restaurants;
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

    public Restaurant openOrCloseRestaurant(RestaurantId restaurantId, boolean isOpen){
        Restaurant restaurant =  findById(restaurantId);
        restaurant.open(isOpen);
        restaurants.save(restaurant);
        return restaurant;
    }

    public void resetOverwrite(RestaurantId restaurantId){
        Restaurant restaurant = findById(restaurantId);
        restaurant.stopOverwriteOpeningHours();
    }

    //Dish
    public Dish findDishById(RestaurantId restaurantId, DishId dishId) {
        return restaurants.findDishById(restaurantId, dishId).orElseThrow(dishId::notFound);
    }

    public Dish updateDish(RestaurantId restaurantId, DishId dishId, String name, String description){
        Restaurant restaurant = findByIdWithMenu(restaurantId);
        Dish dish = restaurant.updateDish(dishId, name, description);

        restaurants.save(restaurant);

        return dish;
    }

    public Dish updateDishState(RestaurantId restaurantId, DishId dishId, DishState state){
       Restaurant restaurant = findByIdWithMenu(restaurantId);
       Dish dish = restaurant.updateDishState(dishId, state);

       restaurants.save(restaurant);

       return dish;
    }

    public List<Dish> publishReadyDishes(RestaurantId restaurantId){
        Restaurant restaurant = findByIdWithMenu(restaurantId);
        List<Dish> readyDishes = findMenuWithDishState(restaurantId,DishState.READY_FOR_PUBLISHING);

        List<Dish> newPublicDished = readyDishes.stream()
                .map(dish -> restaurant.updateDishState(dish.getId(),DishState.PUBLISHED)).toList();

        restaurants.save(restaurant);

        return newPublicDished;
    }

    public List<Dish> publishDishesOnSchedule(RestaurantId id , Date scheduledDate, List<DishId> dishIds){
        Restaurant restaurant = findByIdWithMenu(id);

        //restaurant is saved in PublishDishesTask to the repository
        new Timer().schedule(new PublishDishesTask(restaurants,restaurant, dishIds), scheduledDate);

        return dishIds.stream().map(restaurant::getDish).toList();
    }
}
