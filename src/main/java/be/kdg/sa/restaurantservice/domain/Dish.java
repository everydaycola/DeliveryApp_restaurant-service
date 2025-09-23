package be.kdg.sa.restaurantservice.domain;

public class Dish {
    private DishId id;
    private boolean isLive;
    //state

    public Dish(DishId id, boolean isLive) {
        this.id = id;
        this.isLive = isLive;
    }
}
