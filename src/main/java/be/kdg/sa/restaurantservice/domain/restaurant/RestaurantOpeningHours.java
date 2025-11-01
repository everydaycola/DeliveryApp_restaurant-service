package be.kdg.sa.restaurantservice.domain.restaurant;

import org.jmolecules.ddd.annotation.ValueObject;

import java.time.DayOfWeek;
import java.time.LocalTime;

@ValueObject
public record RestaurantOpeningHours
        (
                DayOfWeek day,
                LocalTime openingTime,
                LocalTime closingTime
        ) {
}
