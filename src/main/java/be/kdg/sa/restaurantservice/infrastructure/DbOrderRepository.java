package be.kdg.sa.restaurantservice.infrastructure;

import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderId;
import be.kdg.sa.restaurantservice.domain.order.OrderRepository;

import java.util.Optional;

public class DbOrderRepository implements OrderRepository {
    @Override
    public Optional<Order> findById(OrderId orderId) {
        return Optional.empty();
    }
}
