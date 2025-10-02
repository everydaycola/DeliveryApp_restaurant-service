package be.kdg.sa.restaurantservice.domain.restaurant;

import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Restaurant {
    private static final int MAX_DISHES = 10;

    private RestaurantId id;
    private OwnerId ownerId;
    private String name;
    private Address address;
    private String contactEmail;
    private RestaurantType type;
    private List<RestaurantOpeningHours> openingHours;
    private PriceCriteria priceCriteria;
    private List<Dish> menu = new ArrayList<>();
    private String logo;
    //Orders


    private Logger logger = Logger.getLogger(Restaurant.class.getName());


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

    public Restaurant(RestaurantId id, OwnerId ownerId, String name, Address address, String contactEmail, RestaurantType type, List<RestaurantOpeningHours> openingHours, PriceCriteria priceCriteria, String logo) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.address = address;
        this.contactEmail = contactEmail;
        this.type = type;
        this.openingHours = openingHours;
        this.priceCriteria = priceCriteria;
        this.logo = logo;
    }

    public static Restaurant newInstance(OwnerId ownerId, String name, Address address, String contactEmail, RestaurantType type, List<RestaurantOpeningHours> openingHours, String logo) {
        return new Restaurant(RestaurantId.create(), ownerId, name, address, contactEmail, type, openingHours, logo);
    }

    public Dish addDish(String dishName, String description) {
        Dish newDish = new Dish(DishId.create(), dishName, description);
        menu.add(newDish);

        logger.log(Level.FINE, String.format("New dish %s added to %s", newDish.getName(), this.getName()));

        return newDish;
    }

    public Dish updateDishState(DishId id, DishState state) {
        Dish dish = this.getDish(id);

        if (state.equals(DishState.PUBLISHED) && menu.size() >= MAX_DISHES) {
            logger.log(Level.WARNING, String.format("Restaurant %s already has %d dishes", this.getName(), MAX_DISHES));
            return dish;
        }
        dish.updateState(state);

        logger.log(Level.FINE, String.format("State of Dish %s has been set to %s", dish.getName(), state.name()));

        return dish;
    }

    //Getters
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

    public List<Dish> getFullMenu() {
        return menu;
    }

    public List<Dish> getPublicMenu() {
        return menu.stream().filter(dish -> dish.getState() == DishState.PUBLISHED).toList();
    }

    public Dish getDish(DishId dishId) {
        return menu.stream().filter(dish -> dish.getId().equals(dishId)).findFirst().orElseThrow();
    }

    public String getLogo() {
        return logo;
    }
}
