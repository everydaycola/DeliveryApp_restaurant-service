package be.kdg.sa.restaurantservice.infrastructure;

import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderId;
import be.kdg.sa.restaurantservice.domain.order.OrderRepository;
import be.kdg.sa.restaurantservice.domain.order.OrderStatus;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import be.kdg.sa.restaurantservice.infrastructure.jpa.order.JpaOrderEntity;
import be.kdg.sa.restaurantservice.infrastructure.jpa.order.JpaOrderRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public class DbOrderRepository implements OrderRepository {
    public final JpaOrderRepository jpaOrderRepository;

    public DbOrderRepository(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public void save(Order order) {
        JpaOrderEntity jpaOrderEntity = JpaOrderEntity.fromDomain(order);
        this.jpaOrderRepository.save(jpaOrderEntity);
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return this.jpaOrderRepository.findById(orderId.id())
                .map(JpaOrderEntity::toDomain);
    }

    @Override
    public Optional<List<Order>> findAllByRestaurantIdAndOrderStatus(RestaurantId id, OrderStatus orderStatus) {
        return this.jpaOrderRepository.findAllByRestaurantIdAndStatus(id.id(), orderStatus).map(
                jpaOrderEntities -> jpaOrderEntities.stream().map(JpaOrderEntity::toDomain).toList()
        );
    }
}
