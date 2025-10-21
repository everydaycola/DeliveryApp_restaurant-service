package be.kdg.sa.restaurantservice.infrastructure.jpa.order;

import be.kdg.sa.restaurantservice.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<JpaOrderEntity, UUID> {
    Optional<List<JpaOrderEntity>> findAllByRestaurantIdAndStatus(UUID restaurantId, OrderStatus status);

    @Query("""
    SELECT o FROM JpaOrderEntity o
    LEFT JOIN FETCH o.orderLines
    WHERE o.orderId = :id
""")
    Optional<JpaOrderEntity> findByIdWithLines(UUID id);
}
