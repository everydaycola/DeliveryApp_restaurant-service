package be.kdg.sa.restaurantservice.domain.order;

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
            throw new IllegalStateException(
                    "Order status should be " + that.getName() + " but is " + this.getName()
            );
        }
    }
}

