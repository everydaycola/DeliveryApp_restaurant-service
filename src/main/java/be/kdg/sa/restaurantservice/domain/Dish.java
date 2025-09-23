package be.kdg.sa.restaurantservice.domain;

public class Dish {
    private DishId id;
    private boolean isLive;
    private DishState state;

    public Dish(DishId id, boolean isLive, DishState state) {
        this.id = id;
        this.isLive = isLive;
        this.state = DishState.NOT_PUBLISHED;
    }
}
