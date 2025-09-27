package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;

import java.util.UUID;

public record RestaurantDto(UUID id, UUID ownerId, String name) {
    public static RestaurantDto from(Restaurant restaurant) {
        return new RestaurantDto(restaurant.getId().id(), restaurant.getOwnerId().id(), restaurant.getName());
    }
}
