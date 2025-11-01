package be.kdg.sa.restaurantservice.api.dtos.restaurant.dish;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@ValueObject
public record DishScheduleDto(Date scheduledDate, List<UUID> dishIds) {
}
