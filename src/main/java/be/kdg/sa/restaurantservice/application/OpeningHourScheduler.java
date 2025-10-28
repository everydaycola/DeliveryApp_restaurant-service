package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
@Slf4j
public class OpeningHourScheduler {

    final RestaurantRepository restaurants;

    public OpeningHourScheduler(RestaurantRepository restaurants) {
        this.restaurants = restaurants;
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