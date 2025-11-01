package be.kdg.sa.restaurantservice.domain.restaurant;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public enum RestaurantType {
    FASTFOOD,
    ITALIAN,
    JAPANESE,
    AMERICAN,
    SANDWICHSHOP
}
