package be.kdg.sa.restaurantservice.api.restaurant.dtos;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record DishScheduleDto(Date scheduledDate, List<UUID> dishIds) {
}
