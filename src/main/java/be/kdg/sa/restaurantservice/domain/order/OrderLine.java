package be.kdg.sa.restaurantservice.domain.order;

import be.kdg.sa.restaurantservice.domain.dish.DishId;
import lombok.Getter;

public class OrderLine {
    @Getter
    private final OrderLineId id;
    @Getter
    private int quantity;
    @Getter
    private final DishId dishId;

    public OrderLine(int quantity, DishId dishId) {
        this.id = OrderLineId.create();
        this.quantity = quantity;
        this.dishId = dishId;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }
}
