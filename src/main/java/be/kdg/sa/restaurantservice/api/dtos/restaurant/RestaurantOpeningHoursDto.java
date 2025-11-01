package be.kdg.sa.restaurantservice.api.dtos.restaurant;

import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantOpeningHours;
import org.jmolecules.ddd.annotation.ValueObject;

import java.time.DayOfWeek;
import java.time.LocalTime;

@ValueObject
public record RestaurantOpeningHoursDto(DayOfWeek day, LocalTime openingTime, LocalTime closingTime) {
    public static RestaurantOpeningHoursDto from(RestaurantOpeningHours openingHours){
        return new RestaurantOpeningHoursDto(openingHours.day(),openingHours.openingTime(),openingHours.closingTime());
    }

    public RestaurantOpeningHours toOpeningHours(){
        return new RestaurantOpeningHours(day,openingTime,closingTime);
    }
}
