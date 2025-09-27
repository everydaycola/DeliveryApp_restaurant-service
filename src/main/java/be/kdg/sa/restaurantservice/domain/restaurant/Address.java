package be.kdg.sa.restaurantservice.domain.restaurant;

public class Address {
    private String street;
    private int number;
    private int postalCode;
    private String country;

    public Address(String street, int number, int postalCode, String country) {
        this.street = street;
        this.number = number;
        this.postalCode = postalCode;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public int getNumber() {
        return number;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }
}
