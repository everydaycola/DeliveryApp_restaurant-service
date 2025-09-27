package be.kdg.sa.restaurantservice.infrastructure;

import be.kdg.sa.restaurantservice.application.RestaurantService;
import be.kdg.sa.restaurantservice.domain.OwnerId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

        restaurantService.create(ownerId, "Pasta Palace");
        restaurantService.create(ownerId, "Sushi World");
        restaurantService.create(ownerId, "Burger Barn");

        System.out.println("Sample restaurants seeded");
    }
}
