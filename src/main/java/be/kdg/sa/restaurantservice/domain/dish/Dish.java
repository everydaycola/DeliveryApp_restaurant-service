package be.kdg.sa.restaurantservice.domain.dish;

public class Dish {
    private DishId id;
    private String name;
    private DishState state;
    private String description;

    public Dish(DishId id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.state = DishState.NOT_PUBLISHED;
    }

    public void updateState(DishState state){
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public DishId getId() {
        return id;
    }

    public DishState getState() {
        return state;
    }
}
