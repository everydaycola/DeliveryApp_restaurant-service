package be.kdg.sa.restaurantservice.infrastructure.jpa.order;


import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderId;
import be.kdg.sa.restaurantservice.domain.order.OrderStatus;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity @Table(name = "orders") public class JpaOrderEntity {
    @Id @Column private UUID orderId;

    @Column private OrderStatus status;

    @Column @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
    private List <JpaOrderLineEntity> orderLines;

    @Column private UUID restaurantId;

    protected JpaOrderEntity() {}

    public JpaOrderEntity(UUID orderId, OrderStatus status, UUID restaurantId) {
        this.orderId = orderId;
        this.status = status;
        this.orderLines = List.of();
        this.restaurantId = restaurantId;
    }

    public static JpaOrderEntity fromDomain(Order order) {
        JpaOrderEntity jpaOrderEntity =
                new JpaOrderEntity(order.getOrderId().id(), order.getStatus(), order.getRestaurantId().id());

        List <JpaOrderLineEntity> jpaOrderEntities =
                order.getOrderLines().stream().map(JpaOrderLineEntity::fromDomain).toList();
        jpaOrderEntity.setOrderLines(jpaOrderEntities);

        return jpaOrderEntity;
    }

    public Order toDomain() {
        Order order = new Order(
                new OrderId(this.orderId),
                new RestaurantId(this.restaurantId)
        );

        order.setStatus(this.status);

        this.orderLines.forEach(jpaOrderLine ->
                order.NewOrderLine(
                        jpaOrderLine.getQuantity(),
                        new DishId(jpaOrderLine.getDishId())));

        return order;
    }

    public void setOrderLines(List <JpaOrderLineEntity> orderLines) {
        this.orderLines = orderLines;
    }
}
