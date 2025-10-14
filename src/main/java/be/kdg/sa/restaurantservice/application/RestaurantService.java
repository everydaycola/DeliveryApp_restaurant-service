package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.api.dtos.AddressDto;
import be.kdg.sa.restaurantservice.api.dtos.RestaurantOpeningHoursDto;
import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.*;
import be.kdg.sa.restaurantservice.domain.restaurant.priceCriteria.MeanPriceCriteriaCalculator;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return restaurants.findDishesByDishState(restaurantId, state).orElseThrow();
    }

    //Update
    //Restaurant
    private void checkIfOpenAndUpdate(Restaurant restaurant){
        restaurant.checkIfOpen();
        restaurants.save(restaurant);
    }

    //Dish
    public Dish UpdateDish(RestaurantId restaurantId, DishId dishId, String name, String description){
        Restaurant restaurant = findByIdWithMenu(restaurantId);
        Dish dish = restaurant.updateDish(dishId, name, description);

        restaurants.save(restaurant);

        return dish;
    }

    public Dish UpdateDishState(RestaurantId restaurantId, DishId dishId, DishState state){
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
}
