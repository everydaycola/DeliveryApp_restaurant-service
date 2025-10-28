package be.kdg.sa.restaurantservice.api.dtos.restaurant;

import be.kdg.sa.restaurantservice.domain.restaurant.Address;

public record AddressDto(String street, int number, int postalCode, String country) {
    public static AddressDto from(Address address){
        return new AddressDto(address.getStreet(), address.getNumber(), address.getPostalCode(), address.getCountry());
    }

    public Address toAddress(){
        return new Address(street, number, postalCode, country);
    }
}
