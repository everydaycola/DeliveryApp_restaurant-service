package be.kdg.sa.restaurantservice.api.dtos.restaurant;

import be.kdg.sa.restaurantservice.domain.restaurant.Address;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record AddressDto(String street, int number, int postalCode, String country) {
    public static AddressDto from(Address address){
        return new AddressDto(address.street(), address.number(), address.postalCode(), address.country());
    }

    public Address toAddress(){
        return new Address(street, number, postalCode, country);
    }
}
