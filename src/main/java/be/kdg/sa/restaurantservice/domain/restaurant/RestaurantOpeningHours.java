package be.kdg.sa.restaurantservice.domain.restaurant;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter public class RestaurantOpeningHours {
    private final DayOfWeek day;
    private final LocalTime openingTime;
    private final LocalTime closingTime;

    public RestaurantOpeningHours(DayOfWeek day, LocalTime openingTime, LocalTime closingTime) {
        this.day = day;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

}
