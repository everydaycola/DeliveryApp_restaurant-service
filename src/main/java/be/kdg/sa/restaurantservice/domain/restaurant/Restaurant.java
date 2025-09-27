package be.kdg.sa.restaurantservice.domain.restaurant;

import be.kdg.sa.restaurantservice.domain.*;

import java.util.List;

public class Restaurant {
    private RestaurantId id;
    private OwnerId ownerId;
    private String name;
    private Address address;
    private String contactEmail;
    private RestaurantType type;
    private List<RestaurantOpeningHours> openingHours;
    private PriceCriteria priceCriteria;
    private List<Dish> dishes;
    private String logo;
    //Orders

    private Restaurant(final RestaurantId id, OwnerId ownerId, String name, Address address, String contactEmail, RestaurantType type, List<RestaurantOpeningHours> openingHours, String logo) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.address = address;
        this.contactEmail = contactEmail;
        this.type = type;
        this.openingHours = openingHours;
        this.logo = logo;
    }

    public static Restaurant newInstance(OwnerId ownerId, String name, Address address, String contactEmail, RestaurantType type, List<RestaurantOpeningHours> openingHours, String logo) {
        return new Restaurant(RestaurantId.create(), ownerId, name, address, contactEmail, type, openingHours, logo);
    }

    public RestaurantId getId() {
        return id;
    }

    public OwnerId getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public RestaurantType getType() {
        return type;
    }

    public List<RestaurantOpeningHours> getOpeningHours() {
        return openingHours;
    }

    public PriceCriteria getPriceCriteria() {
        return priceCriteria;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public String getLogo() {
        return logo;
    }
}
