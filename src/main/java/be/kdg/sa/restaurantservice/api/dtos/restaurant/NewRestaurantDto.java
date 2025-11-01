package be.kdg.sa.restaurantservice.api.dtos.restaurant;

import be.kdg.sa.restaurantservice.domain.restaurant.OwnerId;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantType;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.List;

@ValueObject
public record NewRestaurantDto(
        String name,
        AddressDto address,
        RestaurantType type,
        List<RestaurantOpeningHoursDto> openingHours,
        String logo,
        String contactEmail
) {
    public Restaurant toRestaurant(OwnerId ownerId) {
        Restaurant restaurant = new Restaurant(
                ownerId,
                name,
                address.toAddress(),
                contactEmail,
                type,
                logo
        );
        openingHours.stream()
                .map(RestaurantOpeningHoursDto::toOpeningHours)
                .forEach(restaurant::addOpeningHours);
        return restaurant;
    }
}
