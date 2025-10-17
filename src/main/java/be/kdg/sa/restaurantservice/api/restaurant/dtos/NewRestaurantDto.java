package be.kdg.sa.restaurantservice.api.restaurant.dtos;

import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantType;

import java.util.List;
import java.util.UUID;

public record NewRestaurantDto( UUID ownerId, String name, AddressDto address, String contactEmail, RestaurantType type, List<RestaurantOpeningHoursDto> openingHours, String logo) {
    public static NewRestaurantDto from(Restaurant restaurant) {
        return new NewRestaurantDto(
                restaurant.getOwnerId().id(),
                restaurant.getName(),
                AddressDto.from(restaurant.getAddress()),
                restaurant.getContactEmail(),
                restaurant.getType(),
                restaurant.getOpeningHours().stream().map(RestaurantOpeningHoursDto::from).toList(),
                restaurant.getLogo()
        );
    }
}
