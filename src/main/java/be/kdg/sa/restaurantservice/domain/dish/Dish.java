package be.kdg.sa.restaurantservice.domain.dish;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jmolecules.ddd.annotation.Entity;

@Entity
@Slf4j
@Getter public class Dish {
    private final DishId id;
    private String name;
    private DishState state;
    private String description;
    private final double price;

    public Dish(DishId id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.state = DishState.NOT_PUBLISHED;
    }

    public Dish(DishId id, String name, DishState state, String description, double price) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.description = description;
        this.price = price;
    }

    public Dish updateDish(String name, String description){
        log.info("Updating dish: name={}, description={}", name, description);
        this.name = name;
        this.description = description;
        return this;
    }

    public void updateState(DishState state){
        log.info("Updating dish state: {}", state);
        this.state = state;
    }

}
