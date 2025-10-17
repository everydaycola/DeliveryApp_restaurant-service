package be.kdg.sa.restaurantservice.api.order;

import be.kdg.sa.restaurantservice.api.order.dtos.OrderDto;
import be.kdg.sa.restaurantservice.application.OrderService;
import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderStatus;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orders;
    private final RabbitTemplate rabbitTemplate;

    public OrderController(OrderService orders, RabbitTemplate rabbitTemplate) {
        this.orders = orders;
        this.rabbitTemplate = rabbitTemplate;
    }


    //GET
    @GetMapping("/{restaurantId}/pending")
    public ResponseEntity<List<OrderDto>> findAllPendingOrders(@PathVariable UUID restaurantId){
        final RestaurantId restoId = new RestaurantId(restaurantId);

        List<Order> pendingOrders = orders.findAllByRestaurantIdAndStatus(restoId, OrderStatus.PENDING);

        List<OrderDto> dtos = pendingOrders.stream().map(OrderDto::from).toList();

        return ResponseEntity.ok(dtos);
    }
}
