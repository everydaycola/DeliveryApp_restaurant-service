package be.kdg.sa.restaurantservice.domain.order;

public enum OrderStatus {
    UNCONFIRMED,
    PENDING,
    ACCEPTED,
    DECLINED,
    IN_PREPARATION,
    READY,
    IN_DELIVERY,
    DELIVERED
}