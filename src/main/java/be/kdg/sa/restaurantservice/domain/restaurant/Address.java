package be.kdg.sa.restaurantservice.domain.restaurant;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Address(
        String street,
        int number,
        int postalCode,
        String country
) {
    @Override
    public String toString() {
        return String.format("%s %s %d %d", country, street, number, postalCode);
    }
}
