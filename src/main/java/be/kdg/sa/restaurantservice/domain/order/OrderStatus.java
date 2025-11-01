package be.kdg.sa.restaurantservice.domain.order;

import lombok.extern.slf4j.Slf4j;
import org.jmolecules.ddd.annotation.ValueObject;

@Slf4j
@ValueObject
public enum OrderStatus {
    UNCONFIRMED,
    PENDING,
    ACCEPTED,
    DECLINED,
    READY,
    IN_DELIVERY,
    DELIVERED;

    private String getName() {
        return this.name().replace("_", " ").toLowerCase();
    }

    public void shouldBe(OrderStatus this, OrderStatus that ) {
        if (!this.equals(that)) {
            log.error("Order status should be {} but is {}", that.getName(), this.getName());
            throw new IllegalStateException(
                    "Order status should be " + that.getName() + " but is " + this.getName()
            );
        }
    }
}

