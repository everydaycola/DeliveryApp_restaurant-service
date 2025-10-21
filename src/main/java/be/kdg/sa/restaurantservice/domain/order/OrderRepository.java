package be.kdg.sa.restaurantservice.domain.order;

import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(OrderId orderId);
    Optional<Order> findByIdWithLines(OrderId orderId);
    Optional<List<Order>> findAllByRestaurantIdAndOrderStatus(RestaurantId id, OrderStatus orderStatus);
}
