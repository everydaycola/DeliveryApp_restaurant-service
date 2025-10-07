package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.api.dtos.AddressDto;
import be.kdg.sa.restaurantservice.api.dtos.RestaurantOpeningHoursDto;
import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.*;
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
        final Restaurant restaurant = Restaurant.newInstance(ownerId, name, address, contactEmail, type, logo);

        //Convert RestaurantOpeningHoursDto -> RestaurantOpeningHours
        openingHoursDtos.forEach(rohDto -> restaurant.addOpeningHours(rohDto.day(),rohDto.openingTime(), rohDto.closingTime()));

        restaurants.save(restaurant);

        return restaurant;
    }

    //Dish
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

    public Restaurant findByIdWithMenuAndOpeningHours(RestaurantId restaurantId){
        return restaurants.findByIdWithMenuAndOpeningHours(restaurantId).orElseThrow(restaurantId::notFound);
    }

    public List<Restaurant> findAll() {
        return restaurants.findAll();
    }

    //Update
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
}
