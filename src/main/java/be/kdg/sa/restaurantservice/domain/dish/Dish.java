package be.kdg.sa.restaurantservice.domain.dish;

public class Dish {
    private DishId id;
    private String name;
    private DishState state;
    private String description;
    private double price;

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
        this.name = name;
        this.description = description;
        return this;
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

    public double getPrice() {
        return price;
    }
}
