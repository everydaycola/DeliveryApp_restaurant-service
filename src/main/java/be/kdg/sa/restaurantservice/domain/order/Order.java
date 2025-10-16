package be.kdg.sa.restaurantservice.domain.order;

import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Order {
    @Getter
    private final OrderId orderId;
    @Setter
    @Getter
    private OrderStatus status;
    @Getter
    private final ArrayList<OrderLine> orderLines;
    @Getter
    private final RestaurantId restaurantId;

    public Order(OrderId orderId, RestaurantId restaurantId) {
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.status = OrderStatus.PENDING;
        this.orderLines = new ArrayList<>();
    }

    public void NewOrderLine(int quantity, DishId dishId) {
        var existingOrderLine = this.orderLines.stream()
                .filter(ol -> ol.getDishId().equals(dishId))
                .findFirst();

        if (existingOrderLine.isPresent()) {
            existingOrderLine.get().increaseQuantity(quantity);
        } else {
            this.orderLines.add(new OrderLine(quantity, dishId));
        }

    }
}
