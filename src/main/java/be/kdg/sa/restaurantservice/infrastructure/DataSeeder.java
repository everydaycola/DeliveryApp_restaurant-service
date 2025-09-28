package be.kdg.sa.restaurantservice.infrastructure;

import be.kdg.sa.restaurantservice.api.AddressDto;
import be.kdg.sa.restaurantservice.api.RestaurantOpeningHoursDto;
import be.kdg.sa.restaurantservice.application.RestaurantService;
import be.kdg.sa.restaurantservice.domain.restaurant.OwnerId;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RestaurantService restaurantService;
    private Logger logger = Logger.getLogger(Restaurant.class.getName());

    public DataSeeder(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Override
    public void run(String... args) {
        OwnerId ownerId = new OwnerId(UUID.randomUUID());

        // Dummy address
        AddressDto address = new AddressDto(
                "Cederlaan",
                35,
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
        Restaurant resto1 = restaurantService.create(ownerId,
                "Pasta Palace",
                address,
                "pasta@example.com",
                RestaurantType.ITALIAN,
                openingHours,
                "pasta-logo.png"
        );

        restaurantService.createDish(resto1.getId(),"Spaghetti Bolognese", "Spaghetti with Bolognese sauce");

        logger.log(Level.FINE, "Sample restaurant successfully seeded");
    }
}
