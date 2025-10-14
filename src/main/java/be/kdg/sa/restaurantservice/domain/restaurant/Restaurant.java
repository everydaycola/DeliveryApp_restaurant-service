package be.kdg.sa.restaurantservice.domain.restaurant;

import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.priceCriteria.PriceCriteria;
import be.kdg.sa.restaurantservice.domain.restaurant.priceCriteria.PriceCriteriaCalculator;
import lombok.extern.slf4j.Slf4j;
import org.jmolecules.ddd.annotation.AggregateRoot;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AggregateRoot
@Slf4j
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
    private PriceCriteriaCalculator priceCriteriaCalculator;
    private List<Dish> menu;
    private String logo;
    private boolean isOpen;
    private boolean overwriteOpeningHours;
    //Orders

    private Restaurant(final RestaurantId id, OwnerId ownerId, String name, Address address, String contactEmail, RestaurantType type, String logo, PriceCriteriaCalculator priceCriteriaCalculator) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.address = address;
        this.contactEmail = contactEmail;
        this.type = type;
        this.logo = logo;
        this.openingHours = new ArrayList<>();
        this.menu = new ArrayList<>();
        this.isOpen = false;
        this.overwriteOpeningHours = false;
        this.priceCriteriaCalculator = priceCriteriaCalculator;
        calculatePriceCriteria(priceCriteriaCalculator);
    }

    public Restaurant(RestaurantId id, OwnerId ownerId, String name, Address address, String contactEmail, RestaurantType type, String logo, boolean isOpen, boolean overwriteOpeningHours, PriceCriteriaCalculator priceCriteriaCalculator) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.address = address;
        this.contactEmail = contactEmail;
        this.type = type;
        this.logo = logo;
        this.openingHours = new ArrayList<>();
        this.menu = new ArrayList<>();
        this.isOpen = isOpen;
        this.overwriteOpeningHours = overwriteOpeningHours;
        this.priceCriteriaCalculator = priceCriteriaCalculator;
        calculatePriceCriteria(priceCriteriaCalculator);
    }

    public static Restaurant newInstance(OwnerId ownerId, String name, Address address, String contactEmail, RestaurantType type, String logo, PriceCriteriaCalculator priceCriteriaCalculator) {
        return new Restaurant(RestaurantId.create(), ownerId, name, address, contactEmail, type, logo, priceCriteriaCalculator);
    }

    public void checkIfOpen() {

        log.info("Checking if Restaurant {} is open", this.name);

        LocalDateTime now = LocalDateTime.now();
        isOpen = openingHours.stream()
                .filter(roh -> roh.getDay().equals(now.getDayOfWeek()))
                .anyMatch(roh -> roh.getOpeningTime().isBefore(now.toLocalTime()) &&
                        roh.getClosingTime().isAfter(now.toLocalTime()));
    }

    public void open(boolean isOpen){
        this.isOpen = isOpen;
        this.overwriteOpeningHours = true;

        log.info("Restaurant {} is now {} and standard opening hours are overwritten", this.name, isOpen? "open" : "closed");
    }

    public void stopOverwriteOpeningHours(){
        this.overwriteOpeningHours = false;

        log.info("Restaurant {} returns to normal opening hours", this.name);
    }

    private void calculatePriceCriteria(PriceCriteriaCalculator priceCriteriaCalculator) {
        this.priceCriteria = priceCriteriaCalculator.Calculate(menu);
        log.info("PriceCriteria of {} set to {}",this.name, this.priceCriteria);
    }

    //Dish Aggregate
    public Dish addDish(String dishName, String description, double price) {
        Dish newDish = new Dish(DishId.create(), dishName, description, price);
        this.menu.add(newDish);

        log.info("New dish {} added to {}", newDish.getName(), this.getName());

        return newDish;
    }

    public Dish addDishFromRepository(UUID dishId, String dishName, String description, DishState state, double price) {
        Dish newDish = new Dish(new DishId(dishId), dishName, state, description, price);
        this.menu.add(newDish);

        calculatePriceCriteria(priceCriteriaCalculator);

        log.info("Dish {} added to {} from repository", newDish.getName(), this.getName());

        return newDish;
    }

    public Dish updateDish(DishId id, String name, String description) {
        Dish dish = this.getDish(id);
        dish.updateDish(name, description);
        calculatePriceCriteria(priceCriteriaCalculator);

        log.info("Dish {} updated", dish.getId());

        return dish;
    }

    public Dish updateDishState(DishId id, DishState state) {
        Dish dish = this.getDish(id);

        if (state.equals(DishState.PUBLISHED) && menu.size() >= MAX_DISHES) {
            log.info("Restaurant {} already has {} dishes", this.getName(), MAX_DISHES);
            return dish;
        }
        dish.updateState(state);

        log.info("State of Dish {} has been set to {}", dish.getName(), state.name());

        return dish;
    }

    //OpeningHours Aggregate
    public RestaurantOpeningHours addOpeningHours(DayOfWeek day, LocalTime start, LocalTime end) {
        RestaurantOpeningHours newOpeningHours = new RestaurantOpeningHours(day, start, end);
        if (!openingHours.isEmpty()) {
            if (checkOpeningHoursOverlap(newOpeningHours)) {
                throw new IllegalArgumentException("The new hours overlap with existing hours");
            }
        }
        log.info("New opening hours succesfully added");
        openingHours.add(newOpeningHours);
        return newOpeningHours;
    }

    private boolean checkOpeningHoursOverlap(RestaurantOpeningHours newRoh) {
        log.info("Checking if new hours overlap with current hours");
        return openingHours.stream()
                .filter(roh -> roh.getDay().equals(newRoh.getDay()))
                .anyMatch(roh -> roh.getOpeningTime().isBefore(newRoh.getClosingTime()) ||
                        roh.getClosingTime().isAfter(newRoh.getOpeningTime()));
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

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isOverwriteOpeningHours() {
        return overwriteOpeningHours;
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
