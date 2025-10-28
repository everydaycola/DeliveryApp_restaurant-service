package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.order.OrderId;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@EnableScheduling
@Slf4j
public class RestaurantTaskScheduler {

    private final TaskScheduler taskScheduler;
    private final ScheduledEvents scheduledEvents;

    public RestaurantTaskScheduler(TaskScheduler taskScheduler, ScheduledEvents scheduledEvents) {
        this.taskScheduler = taskScheduler;
        this.scheduledEvents = scheduledEvents;
    }

    public void ScheduleDishPublishing(Date scheduledDate, List<DishId> dishIds, Restaurant restaurant, int maxDishes) {
        log.info("Scheduling task to run at: {}", scheduledDate);
        taskScheduler.schedule(() -> {
            scheduledEvents.publishDishes(scheduledDate, dishIds, restaurant, maxDishes);
        }, scheduledDate.toInstant());
    }

    public void StartOrderTimeOut(RestaurantId restaurantId, OrderId orderId, long timeoutInSeconds) {
        log.info("Scheduling task to run in {} seconds", timeoutInSeconds);
        taskScheduler.schedule(() ->
                        scheduledEvents.handlePendingOrderTimeout(restaurantId, orderId),
                Instant.now().plusSeconds(timeoutInSeconds));

    }

    // every minute
    @Scheduled(cron = "0 * * * * ?")
    public void CheckOverrides() {
        scheduledEvents.checkOverrides();
    }
}
