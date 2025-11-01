package be.kdg.sa.restaurantservice.api.dtos.restaurant.dish;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record UpdateDishDto(String name, String description, double price) {
}
