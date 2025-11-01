package be.kdg.sa.restaurantservice.api.dtos.restaurant;

import be.kdg.sa.restaurantservice.api.dtos.restaurant.dish.MenuCountsDto;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantType;
import org.jmolecules.ddd.annotation.ValueObject;

import java.util.List;
import java.util.UUID;

@ValueObject
public record RestaurantDto(UUID id, UUID ownerId, String name, AddressDto address, String contactEmail, RestaurantType type, List<RestaurantOpeningHoursDto> openingHours, String logo, MenuCountsDto menuCounts, boolean isOpen, String priceCriteria) {
    public static RestaurantDto from(Restaurant restaurant) {
        return new RestaurantDto(
                restaurant.getId().id(),
                restaurant.getOwnerId().id(),
                restaurant.getName(),
                AddressDto.from(restaurant.getAddress()),
                restaurant.getContactEmail(),
                restaurant.getType(),
                restaurant.getOpeningHours().stream().map(RestaurantOpeningHoursDto::from).toList(),
                restaurant.getLogo(),
                MenuCountsDto.from(restaurant.getFullMenu()),
                restaurant.isOpen(),
                restaurant.getPriceCriteria().getDescription()
                );
    }
}
