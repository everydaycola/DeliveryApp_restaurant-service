package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.api.AddressDto;
import be.kdg.sa.restaurantservice.api.RestaurantOpeningHoursDto;
import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurants;

    public RestaurantService(@Qualifier("dbRestaurantRepository") RestaurantRepository restaurants) {
        this.restaurants = restaurants;
    }

    //Create
    public Restaurant create(OwnerId ownerId, String name, AddressDto addressDto, String contactEmail, RestaurantType type, List<RestaurantOpeningHoursDto> openingHoursDto, String logo) {
        //Convert AddressDto -> Address
        Address address = new Address(addressDto.street(), addressDto.number(), addressDto.postalCode(), addressDto.country());

        //Convert RestaurantOpeningHoursDto -> RestaurantOpeningHours
        List<RestaurantOpeningHours> openingHours = new ArrayList<>();
        openingHoursDto.forEach(rohDto -> openingHours.add(new RestaurantOpeningHours(rohDto.day(),rohDto.openingTime(),rohDto.closingTime())));

        //Create & Save Restaurant
        final Restaurant restaurant = Restaurant.newInstance(ownerId, name, address, contactEmail, type, openingHours, logo);
        restaurants.save(restaurant);

        return restaurant;
    }

    public Dish createDish(RestaurantId id, String dishName, String description){
        Restaurant restaurant = restaurants.findByIdWithMenu(id).orElseThrow(id::notFound);
        Dish dish = restaurant.addDish(dishName, description);
        restaurants.save(restaurant);
        return dish;
    }

    //Find
    public Restaurant findById(RestaurantId restaurantId) {
        return restaurants.findById(restaurantId).orElseThrow(restaurantId::notFound);
    }

    public Restaurant findByIdWithMenu(RestaurantId restaurantId){
        return restaurants.findByIdWithMenu(restaurantId).orElseThrow(restaurantId::notFound);
    }

    public List<Restaurant> findAll() {
        return restaurants.findAll();
    }

    //Update
    public Dish UpdateDishState(RestaurantId restaurantId, DishId dishId, DishState state){
       Restaurant restaurant = findByIdWithMenu(restaurantId);
       Dish dish = restaurant.updateDishState(dishId, state);

       restaurants.save(restaurant);

       return dish;
    }
}
