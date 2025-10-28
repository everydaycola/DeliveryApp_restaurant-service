package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.config.DomainProperties;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.order.*;
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
        return orders.findById(orderId).orElseThrow(orderId::notFound);
    }

    public Order findByIdWithLines(OrderId orderId){
        return orders.findByIdWithLines(orderId).orElseThrow(orderId::notFound);
    }

    public List<Order> findAllByRestaurantIdAndStatus(RestaurantId restaurantId, OrderStatus status) {
        return orders.findAllByRestaurantIdAndOrderStatus(restaurantId, status).orElseThrow(restaurantId::notFound);
    }

    private Order findByRestaurantIdAndOrderId(RestaurantId restaurantId, OrderId orderId){
        return orders.findByRestaurantIdAndOrderId(restaurantId,orderId).orElseThrow(orderId::notFound);
    }

    private Order findByRestaurantIdAndOrderIdAndStatus(RestaurantId restaurantId, OrderId orderId, OrderStatus orderStatus){
        return orders.findByRestaurantIdAndOrderIdAndOrderStatus(restaurantId,orderId,orderStatus).orElseThrow(orderId::notFound);
    }

    public void placeOrder(OrderPlacedMessage message) {
        RestaurantId resId = new RestaurantId(UUID.fromString(message.orderDto().restaurantId()));
        OrderId ordId = new OrderId(UUID.fromString(message.orderDto().orderId()));

        Order order = new Order(ordId, resId);
        order.setStatus(OrderStatus.valueOf(message.orderDto().status()));

        orders.save(order);
        log.info("Order {} successfully placed", order.getOrderId().id());

        message.orderDto().orderLines().forEach(orderLineDto ->
                addOrderLineToOrder(order.getOrderId().id(),orderLineDto.amount(),new DishId(UUID.fromString(orderLineDto.dishId())))
        );
        log.info("All lines of Order {} have been successfully added", order.getOrderId());

        restaurantTaskScheduler.StartOrderTimeOut(resId, ordId, domainProperties.getOrderTimeout());
    }

    private void addOrderLineToOrder(UUID orderId, int quantity, DishId dishId){
        OrderId orderID = new OrderId(orderId);
        Order order = findByIdWithLines(orderID);
        order.newOrderLine(quantity, dishId);
        orders.save(order);
        log.info("Dish {} added to Order {}",dishId.id(),order.getOrderId().id());
    }

    public Order acceptOrder(RestaurantId restaurantId, OrderId orderId, boolean accept){
        Order order = findByRestaurantIdAndOrderId(restaurantId,orderId);
        order.acceptOrReject(accept);
        orders.save(order);
        log.info("Order {} accepted", order.getOrderId().id());
        return order;
    }

    public Order readyOrder(RestaurantId restaurantId, OrderId orderId){
        Order order = findByRestaurantIdAndOrderId(restaurantId, orderId);
        order.ready();
        orders.save(order);
        log.info("Order {} set ready for pickup", order.getOrderId().id());
        return order;
    }
}
