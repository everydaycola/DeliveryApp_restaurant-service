package be.kdg.sa.restaurantservice.domain.order;

import be.kdg.sa.restaurantservice.domain.dish.DishId;

public class OrderLine {
    private final OrderLineId id;
    private int quantity;
    private final DishId dishId;

    public OrderLine(int quantity, DishId dishId) {
        this.id = OrderLineId.create();
        this.quantity = quantity;
        this.dishId = dishId;
    }
}
