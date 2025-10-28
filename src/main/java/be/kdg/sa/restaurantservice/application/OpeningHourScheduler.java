package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
@EnableScheduling
@Slf4j
public class OpeningHourScheduler {

    private final TaskScheduler taskScheduler;
    private final RestaurantRepository restaurants;

    public OpeningHourScheduler(TaskScheduler taskScheduler, RestaurantRepository restaurants) {
        this.taskScheduler = taskScheduler;
        this.restaurants = restaurants;
    }

    public ScheduledFuture<?> scheduleTask(Runnable task, LocalDateTime runTime) {
        Instant instant = runTime.atZone(ZoneId.systemDefault()).toInstant();
        log.info("Scheduling task to run at: {}", runTime);
        return taskScheduler.schedule(task, instant);
    }

    // every minute
    @Scheduled(cron = "0 * * * * ?")
    public void CheckOverrides() {
        log.info("Checking for open/close overrides");
        List<Restaurant> restaurantList = restaurants.findAllWithOverride();
        if(restaurantList.isEmpty()) return;
        restaurantList.stream()
                .filter(r -> r.isOpen() == r.getOverrideStatus().getIsOpen())
                .forEach(r -> {
                    r.stopOverwriteOpeningHours();
                    restaurants.save(r);
                });
    }
}