package be.kdg.sa.restaurantservice.api.dtos;


import be.kdg.sa.restaurantservice.domain.order.OrderLine;

public record OrderLineDto(
        String dishId,
        int amount
) {
    public static OrderLineDto from(final OrderLine orderLine) {
        return new OrderLineDto(
                orderLine.getDishId().id().toString(),
                orderLine.getQuantity()
        );
    }
}
