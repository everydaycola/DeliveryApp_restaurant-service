package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.config.DomainProperties;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderId;
import be.kdg.sa.restaurantservice.domain.order.OrderRepository;
import be.kdg.sa.restaurantservice.domain.order.OrderStatus;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages.OrderPlacedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {
    private final OrderRepository orders;
    private final RestaurantTaskScheduler restaurantTaskScheduler;
    private final DomainProperties domainProperties;

    public OrderService(OrderRepository orders, RestaurantTaskScheduler restaurantTaskScheduler, DomainProperties domainProperties) {
        this.orders = orders;
        this.restaurantTaskScheduler = restaurantTaskScheduler;
        this.domainProperties = domainProperties;
    }

    public Order findById(OrderId orderId) {
        log.info("Finding order {}", orderId.id());
        return orders.findById(orderId).orElseThrow(orderId::notFound);
    }

    public Order findByIdWithLines(OrderId orderId){
        log.info("Finding order with lines {}", orderId.id());
        return orders.findByIdWithLines(orderId).orElseThrow(orderId::notFound);
    }

    public List<Order> findAllByRestaurantIdAndStatus(RestaurantId restaurantId, OrderStatus status) {
        log.info("Finding all orders for restaurant {} with status {}", restaurantId.id(), status);
        return orders.findAllByRestaurantIdAndOrderStatus(restaurantId, status).orElseThrow(restaurantId::notFound);
    }

    private Order findByRestaurantIdAndOrderId(RestaurantId restaurantId, OrderId orderId){
        log.info("Finding order for restaurant {} and order {}", restaurantId.id(), orderId.id());
        return orders.findByRestaurantIdAndOrderId(restaurantId,orderId).orElseThrow(orderId::notFound);
    }

    private Order findByRestaurantIdAndOrderIdAndStatus(RestaurantId restaurantId, OrderId orderId, OrderStatus orderStatus){
        log.info("Finding order for restaurant {} and order {} with status {}", restaurantId.id(), orderId.id(), orderStatus);
        return orders.findByRestaurantIdAndOrderIdAndOrderStatus(restaurantId,orderId,orderStatus).orElseThrow(orderId::notFound);
    }

    public void placeOrder(OrderPlacedMessage message) {
        log.info("Placing order for restaurant {}", message.orderDto().restaurantId());
        final var resId = new RestaurantId(UUID.fromString(message.orderDto().restaurantId()));
        final var ordId = new OrderId(UUID.fromString(message.orderDto().orderId()));

        final var order = new Order(ordId, resId);
        order.setStatus(OrderStatus.valueOf(message.orderDto().status()));

        orders.save(order);

        message.orderDto().orderLines().forEach(orderLineDto ->
                addOrderLineToOrder(order.getOrderId().id(),orderLineDto.amount(),new DishId(UUID.fromString(orderLineDto.dishId())))
        );

        restaurantTaskScheduler.StartOrderTimeOut(resId, ordId, domainProperties.getOrderTimeout());
    }

    private void addOrderLineToOrder(UUID orderId, int quantity, DishId dishId){
        log.info("Adding line to order {} with quantity {} and dish {}", orderId, quantity, dishId);
        final var orderID = new OrderId(orderId);
        final var order = findByIdWithLines(orderID);
        order.newOrderLine(quantity, dishId);
        orders.save(order);
    }

    public Order acceptOrder(RestaurantId restaurantId, OrderId orderId, boolean accept, String reason){
        log.info("Accepting order {} for restaurant {} with result {}", orderId.id(), restaurantId.id(), accept);
        final var order = findByRestaurantIdAndOrderId(restaurantId,orderId);
        order.acceptOrReject(accept);
        order.setComment(reason);
        orders.save(order);
        return order;
    }

    public Order readyOrder(RestaurantId restaurantId, OrderId orderId){
        log.info("Readying order {} for restaurant {}", orderId.id(), restaurantId.id());
        final var order = findByRestaurantIdAndOrderId(restaurantId, orderId);
        order.ready();
        orders.save(order);
        return order;
    }
}
