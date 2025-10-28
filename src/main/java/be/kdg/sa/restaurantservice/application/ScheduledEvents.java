package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.order.OrderId;
import be.kdg.sa.restaurantservice.domain.order.OrderRepository;
import be.kdg.sa.restaurantservice.domain.order.OrderStatus;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ScheduledEvents {
    private final RestaurantRepository restaurants;
    private final OrderRepository orderRepository;

    public ScheduledEvents(RestaurantRepository restaurants, OrderRepository orderRepository) {
        this.restaurants = restaurants;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void handlePendingOrderTimeout(RestaurantId restaurantId, OrderId orderId) {
        log.info("Checking timeout for order {}", orderId.id());
        orderRepository.findByRestaurantIdAndOrderIdAndOrderStatus(restaurantId, orderId, OrderStatus.PENDING)
                .ifPresent(o -> {
                    log.info("Order {} timed out", orderId.id());
                    o.acceptOrReject(false);
                    orderRepository.save(o);
                });
    }

    @Transactional
    public void publishDishes(Date scheduledDate, List<DishId> dishIds, Restaurant restaurant, int maxDishes) {
        log.info("Publishing dishes at: {}", scheduledDate);
        dishIds.forEach(dishId -> restaurant.updateDishState(dishId, DishState.PUBLISHED, maxDishes));
    }

    @Transactional
    public void checkOverrides() {
        log.info("Checking for open/close overrides");
        List<Restaurant> restaurantList = restaurants.findAllWithOverride();
        restaurantList.stream()
                .filter(r -> r.isOpen() == r.getOverrideStatus().getIsOpen())
                .forEach(r -> {
                    r.stopOverwriteOpeningHours();
                    restaurants.save(r);
                });
    }
}
