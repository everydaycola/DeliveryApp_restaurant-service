package be.kdg.sa.restaurantservice.domain.order;

import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(OrderId orderId);
}
