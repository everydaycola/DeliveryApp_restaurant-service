package be.kdg.sa.restaurantservice.infrastructure;

import be.kdg.sa.restaurantservice.api.AddressDto;
import be.kdg.sa.restaurantservice.api.RestaurantOpeningHoursDto;
import be.kdg.sa.restaurantservice.application.RestaurantService;
import be.kdg.sa.restaurantservice.domain.restaurant.Address;
import be.kdg.sa.restaurantservice.domain.OwnerId;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantOpeningHours;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RestaurantService restaurantService;

    public DataSeeder(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Override
    public void run(String... args) {
        OwnerId ownerId = new OwnerId(UUID.randomUUID());

        // Dummy address
        AddressDto address = new AddressDto(
                "Cederlaan",
                25,
                2600,
                "Belgium"
        );

        LocalTime startTime = LocalTime.of(8,0);
        LocalTime endTime = LocalTime.of(21,0);

        // Dummy opening hours (adjust to your RestaurantOpeningHours constructor)
        List<RestaurantOpeningHoursDto> openingHours = List.of(
                new RestaurantOpeningHoursDto(DayOfWeek.TUESDAY, startTime, endTime),
                new RestaurantOpeningHoursDto(DayOfWeek.WEDNESDAY, startTime, endTime),
                new RestaurantOpeningHoursDto(DayOfWeek.THURSDAY, startTime, endTime),
                new RestaurantOpeningHoursDto(DayOfWeek.FRIDAY, startTime, endTime),
                new RestaurantOpeningHoursDto(DayOfWeek.SATURDAY, startTime, endTime),
                new RestaurantOpeningHoursDto(DayOfWeek.SUNDAY, startTime, endTime)
        );

        // Create sample restaurants
        restaurantService.create(ownerId,
                "Pasta Palace",
                address,
                "pasta@example.com",
                RestaurantType.ITALIAN,
                openingHours,
                "pasta-logo.png"
        );

        restaurantService.create(ownerId,
                "Sushi World",
                address,
                "sushi@example.com",
                RestaurantType.JAPANESE,
                openingHours,
                "sushi-logo.png"
        );

        restaurantService.create(ownerId,
                "Burger Barn",
                address,
                "burger@example.com",
                RestaurantType.AMERICAN,
                openingHours,
                "burger-logo.png"
        );

        System.out.println("Sample restaurants seeded");
    }
}
