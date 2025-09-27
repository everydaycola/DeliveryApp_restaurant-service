package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.application.RestaurantService;
import be.kdg.sa.restaurantservice.domain.OwnerId;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    //temp solution until we get Auth context
    private static final OwnerId ownerId = new OwnerId(UUID.randomUUID());

    private final RestaurantService restaurants;

    public RestaurantController(RestaurantService restaurants) {
        this.restaurants = restaurants;
    }

    @PostMapping
    public ResponseEntity<RestaurantDto> create(@RequestBody RestaurantDto restaurantDto){
        restaurants.create(ownerId, restaurantDto.name());
        return ResponseEntity.ok(restaurantDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> findById(@PathVariable final UUID id) {
        final RestaurantId restaurantId = new RestaurantId(id);
        final Restaurant restaurant = restaurants.findById(restaurantId);
        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> findAll(){
        List<Restaurant> allRestaurants = restaurants.findAll();

        List<RestaurantDto> dtos = allRestaurants.stream()
                .map(RestaurantDto::from)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
