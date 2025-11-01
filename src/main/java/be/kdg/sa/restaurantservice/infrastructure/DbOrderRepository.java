package be.kdg.sa.restaurantservice.infrastructure;

import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderId;
import be.kdg.sa.restaurantservice.domain.order.OrderRepository;
import be.kdg.sa.restaurantservice.domain.order.OrderStatus;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import be.kdg.sa.restaurantservice.infrastructure.jpa.order.JpaOrderEntity;
import be.kdg.sa.restaurantservice.infrastructure.jpa.order.JpaOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class DbOrderRepository implements OrderRepository {
    public final JpaOrderRepository jpaOrderRepository;

    public DbOrderRepository(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public void save(Order order) {
        log.info("Saving order {}", order.getOrderId().id());
        JpaOrderEntity jpaOrderEntity = JpaOrderEntity.fromDomain(order);
        this.jpaOrderRepository.save(jpaOrderEntity);
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        log.info("Finding order {}", orderId.id());
        return this.jpaOrderRepository.findById(orderId.id())
                .map(JpaOrderEntity::toDomain);
    }

    @Override
    public Optional<Order> findByIdWithLines(OrderId orderId) {
        log.info("Finding order with lines {}", orderId.id());
        return this.jpaOrderRepository.findByIdWithLines(orderId.id())
                .map(JpaOrderEntity::toDomain);
    }

    @Override
    public Optional<List<Order>> findAllByRestaurantIdAndOrderStatus(RestaurantId id, OrderStatus orderStatus) {
        log.info("Finding all orders for restaurant {} with status {}", id.id(), orderStatus);
        return this.jpaOrderRepository.findAllByRestaurantIdAndStatus(id.id(), orderStatus).map(
                jpaOrderEntities -> jpaOrderEntities.stream().map(JpaOrderEntity::toDomain).toList()
        );
    }

    @Override
    public Optional<Order> findByRestaurantIdAndOrderId(RestaurantId restaurantId, OrderId orderId) {
        log.info("Finding order for restaurant {} and order {}", restaurantId.id(), orderId.id());
        return this.jpaOrderRepository.findByRestaurantIdAndOrderId(restaurantId.id(), orderId.id())
                .map(JpaOrderEntity::toDomain);
    }

    @Override
    public Optional<Order> findByRestaurantIdAndOrderIdAndOrderStatus(RestaurantId restaurantId, OrderId orderId, OrderStatus orderStatus) {
        log.info("Finding order for restaurant {} and order {} with status {}", restaurantId.id(), orderId.id(), orderStatus);
        return this.jpaOrderRepository.findByRestaurantIdAndOrderIdAndStatus(restaurantId.id(), orderId.id(), orderStatus)
                .map(JpaOrderEntity::toDomain);
    }
}
