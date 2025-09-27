package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.domain.restaurant.Address;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantOpeningHours;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantType;

import java.util.List;
import java.util.UUID;

public record RestaurantDto(UUID id, UUID ownerId, String name, AddressDto address, String contactEmail, RestaurantType type, List<RestaurantOpeningHoursDto> openingHours, String logo) {
    public static RestaurantDto from(Restaurant restaurant) {
        return new RestaurantDto(
                restaurant.getId().id(),
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
