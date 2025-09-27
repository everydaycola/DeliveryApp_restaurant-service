package be.kdg.sa.restaurantservice.domain.restaurant;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class RestaurantOpeningHours {
    private DayOfWeek day;
    private LocalTime openingTime;
    private LocalTime closingTime;

    public RestaurantOpeningHours(DayOfWeek day, LocalTime openingTime, LocalTime closingTime) {
        this.day = day;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }
}
