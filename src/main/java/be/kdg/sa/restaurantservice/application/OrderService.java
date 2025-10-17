package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderRepository;
import be.kdg.sa.restaurantservice.domain.order.OrderStatus;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orders;

    public OrderService(OrderRepository orders) {
        this.orders = orders;
    }

    public List<Order> findAllByRestaurantIdAndStatus(RestaurantId restaurantId, OrderStatus status){
        return orders.findAllByRestaurantIdAndOrderStatus(restaurantId,status).orElseThrow(restaurantId::notFound);
    }
}
