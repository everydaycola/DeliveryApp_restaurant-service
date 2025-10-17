package be.kdg.sa.restaurantservice.infrastructure.jpa.order;

import be.kdg.sa.restaurantservice.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<JpaOrderEntity, UUID> {
    Optional<List<JpaOrderEntity>> findAllByRestaurantIdAndStatus(UUID restaurantId, OrderStatus status);
}
