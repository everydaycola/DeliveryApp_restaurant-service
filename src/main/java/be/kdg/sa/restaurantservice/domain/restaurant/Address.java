package be.kdg.sa.restaurantservice.domain.restaurant;

import lombok.Getter;

@Getter public class Address {
    private final String street;
    private final int number;
    private final int postalCode;
    private final String country;

    public Address(String street, int number, int postalCode, String country) {
        this.street = street;
        this.number = number;
        this.postalCode = postalCode;
        this.country = country;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d %d", country, street,number,postalCode);
    }
}
