package be.kdg.sa.restaurantservice.domain;

public class Dish {
    private DishId id;
    private boolean isLive;
    private DishState state;

    public Dish(DishId id, boolean isLive) {
        this.id = id;
        this.isLive = isLive;
        this.state = DishState.NOT_PUBLISHED;
    }

    public DishId getId() {
        return id;
    }

    public boolean isLive() {
        return isLive;
    }

    public DishState getState() {
        return state;
    }
}
