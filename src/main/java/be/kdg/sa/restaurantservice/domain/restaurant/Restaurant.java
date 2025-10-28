package be.kdg.sa.restaurantservice.domain.restaurant;
import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.priceCriteria.MeanPriceCriteriaCalculator;
import be.kdg.sa.restaurantservice.domain.restaurant.priceCriteria.PriceCriteria;
import be.kdg.sa.restaurantservice.domain.restaurant.priceCriteria.PriceCriteriaCalculator;
import lombok.Getter;
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
    @Getter
    private final RestaurantId id;
    @Getter
    private final OwnerId ownerId;
    @Getter
    private final String name;
    @Getter
    private final Address address;
    @Getter
    private final String contactEmail;
    @Getter
    private final RestaurantType type;
    @Getter
    private final List<RestaurantOpeningHours> openingHours;
    @Getter
    private static PriceCriteria priceCriteria;
    private PriceCriteriaCalculator priceCriteriaCalculator;
    private final List<Dish> menu;
    @Getter
    private final String logo;
    @Getter
    private boolean isOpen;
    @Getter
    private OverrideStatus overrideStatus;

    public Restaurant(OwnerId ownerId, String name, Address address, String contactEmail, RestaurantType type, String logo) {
        this(RestaurantId.create(), ownerId, name, address, contactEmail, type, logo, new ArrayList<>(), new ArrayList<>(), OverrideStatus.NONE, new MeanPriceCriteriaCalculator());
    }

    public Restaurant(
            RestaurantId id,
            OwnerId ownerId,
            String name,
            Address address,
            String contactEmail,
            RestaurantType type,
            String logo,
            List<RestaurantOpeningHours> openingHours,
            List<Dish> menu,
            OverrideStatus overrideStatus,
            PriceCriteriaCalculator priceCriteriaCalculator
    ) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.address = address;
        this.contactEmail = contactEmail;
        this.type = type;
        this.logo = logo;
        this.openingHours = openingHours;
        this.menu = menu;
        this.overrideStatus = overrideStatus;
        this.priceCriteriaCalculator = priceCriteriaCalculator;
        this.isOpen = checkIfOpen();
    }

    private boolean checkIfOpen() {
        if (!this.overrideStatus.equals(OverrideStatus.NONE)) return this.overrideStatus.getIsOpen();
        LocalDateTime now = LocalDateTime.now();
        return openingHours.stream()
                .filter(roh -> roh.getDay().equals(now.getDayOfWeek()))
                .anyMatch(roh -> roh.getOpeningTime().isBefore(now.toLocalTime()) &&
                        roh.getClosingTime().isAfter(now.toLocalTime()));
    }

    public void open(boolean isOpen) {
        this.isOpen = isOpen;
        this.overrideStatus = OverrideStatus.fromBoolean(isOpen);

        log.info("Restaurant {} is now {} and standard opening hours are overwritten", this.name, isOpen? "open" : "closed");
    }

    public void stopOverwriteOpeningHours(){
        this.overrideStatus = OverrideStatus.NONE;

        log.info("Restaurant {} returns to normal opening hours", this.name);
    }

    private void calculatePriceCriteria() {
        priceCriteria = priceCriteriaCalculator.calculate(menu);
        log.info("PriceCriteria of {} set to {}",this.name, priceCriteria);
    }

    // unused currently but allows future strategy changes
    public void changePriceCalculatorStrategy(PriceCriteriaCalculator priceCriteriaCalculator) {
        this.priceCriteriaCalculator = priceCriteriaCalculator;
        calculatePriceCriteria();
    }

    //Dish Aggregate
    public Dish addDish(String dishName, String description, double price) {
        Dish newDish = new Dish(DishId.create(), dishName, description, price);
        this.menu.add(newDish);

        log.info("New dish {} added to {}", newDish.getName(), this.getName());

        this.calculatePriceCriteria();

        return newDish;
    }

    public Dish updateDish(DishId id, String name, String description) {
        Dish dish = this.getDish(id);
        dish.updateDish(name, description);
        calculatePriceCriteria();

        log.info("Dish {} updated", dish.getId());

        return dish;
    }

    public Dish updateDishState(DishId id, DishState state, int maxDishes) {
        Dish dish = this.getDish(id);

        if (state.equals(DishState.PUBLISHED) && menu.size() >= maxDishes) {
            log.info("Restaurant {} has {} dishes, the max is {}", this.getName(), menu.size(), maxDishes);
            throw new IllegalStateException("Restaurant already has " + menu.size() + " out of " + maxDishes + " dishes");
        }
        dish.updateState(state);

        log.info("State of Dish {} has been set to {}", dish.getName(), state.name());

        return dish;
    }

    public List<Dish> getFullMenu() {
        return menu;
    }

    public Dish getDish(DishId dishId) {
        return menu.stream().filter(dish -> dish.getId().equals(dishId)).findFirst().orElseThrow();
    }

    //OpeningHours Aggregate
    public void addOpeningHours(RestaurantOpeningHours newOpeningHours) {
        if (!this.openingHours.isEmpty() && checkOpeningHoursOverlap(newOpeningHours)) {
            throw new IllegalArgumentException("The new hours overlap with existing hours");
        }
        log.info("New opening hours succesfully added");
        this.openingHours.add(newOpeningHours);
    }

    private boolean checkOpeningHoursOverlap(RestaurantOpeningHours newRoh) {
        log.info("Checking if new hours overlap with current hours");
        return openingHours.stream()
                .filter(roh -> roh.getDay().equals(newRoh.getDay()))
                .anyMatch(roh -> roh.getOpeningTime().isBefore(newRoh.getClosingTime()) ||
                        roh.getClosingTime().isAfter(newRoh.getOpeningTime()));
    }

    public void checkIfOwnerBy(OwnerId ownerId) {
        log.info("Authenticating restaurant {} for courier {}", this.id, ownerId);
        if(!this.ownerId.equals(ownerId)) {
            log.warn("Restaurant {} is not owned by {} but it is owned by {}", this.id, ownerId, this.ownerId);
            throw new IllegalStateException("Restaurant is owner by " + this.ownerId.id() + " and not " + ownerId.id());
        }
    }


}
