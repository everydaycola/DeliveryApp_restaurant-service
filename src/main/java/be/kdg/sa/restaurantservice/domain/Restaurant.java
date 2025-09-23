package be.kdg.sa.restaurantservice.domain;

import java.util.List;

public class Restaurant {
    private RestaurantId id;
    private String name;
    private Address address;
    private String contactEmail;
    private RestaurantType type;
    private List<RestaurantOpeningHours> openingHours;
    private PriceCriteria priceCriteria;
    private List<Dish> dishes;
    private String logo;
    //Orders

    public Restaurant(RestaurantId id, String name, Address address, String contactEmail, RestaurantType type, List<RestaurantOpeningHours> openingHours, String logo) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contactEmail = contactEmail;
        this.type = type;
        this.openingHours = openingHours;
        this.logo = logo;
    }


}
