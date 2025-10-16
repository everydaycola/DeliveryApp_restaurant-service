package be.kdg.sa.restaurantservice.infrastructure.jpa.order;


import be.kdg.sa.restaurantservice.domain.order.OrderLine;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Table(name = "orderLines")
@Getter
public class JpaOrderLineEntity {
    @Id
    @Column
    private UUID dishId;

    @Column
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private JpaOrderEntity order;

    protected JpaOrderLineEntity() {}

    public JpaOrderLineEntity(UUID dishId, int quantity) {
        this.dishId = dishId;
        this.quantity = quantity;
    }

    public static JpaOrderLineEntity fromDomain(OrderLine orderLine) {
        return new JpaOrderLineEntity(
                orderLine.getId().id(),
                orderLine.getQuantity()
        );
    }
}
