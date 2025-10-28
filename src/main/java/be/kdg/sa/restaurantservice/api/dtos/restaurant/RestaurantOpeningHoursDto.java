package be.kdg.sa.restaurantservice.api.dtos.restaurant;

import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantOpeningHours;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record RestaurantOpeningHoursDto(DayOfWeek day, LocalTime openingTime, LocalTime closingTime) {
    public static RestaurantOpeningHoursDto from(RestaurantOpeningHours openingHours){
        return new RestaurantOpeningHoursDto(openingHours.getDay(),openingHours.getOpeningTime(),openingHours.getClosingTime());
    }

    public RestaurantOpeningHours toOpeningHours(){
        return new RestaurantOpeningHours(day,openingTime,closingTime);
    }
}
