package be.kdg.sa.restaurantservice.domain.order;

import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;

import java.util.ArrayList;

public class Order {
    private final OrderId orderId;
    private OrderStatus status;
    private final ArrayList<OrderLine> orderLines;
    private final RestaurantId restaurantId;

    public Order(OrderId orderId, RestaurantId restaurantId) {
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.status = OrderStatus.PENDING;
        this.orderLines = new ArrayList<>();
    }
}
