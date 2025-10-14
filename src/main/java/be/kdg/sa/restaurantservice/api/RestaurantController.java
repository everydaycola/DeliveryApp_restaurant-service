package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.api.dtos.DishDto;
import be.kdg.sa.restaurantservice.api.dtos.NewRestaurantDto;
import be.kdg.sa.restaurantservice.api.dtos.RestaurantDto;
import be.kdg.sa.restaurantservice.application.RestaurantService;
import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.OwnerId;
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

    //POST
    //Restaurant
    @PostMapping
    public ResponseEntity<NewRestaurantDto> create(@RequestBody RestaurantDto restaurantDto) {
        Restaurant restaurant = restaurants.create(
                ownerId,
                restaurantDto.name(),
                restaurantDto.address(),
                restaurantDto.contactEmail(),
                restaurantDto.type(),
                restaurantDto.openingHours(),
                restaurantDto.logo());

        NewRestaurantDto result = NewRestaurantDto.from(restaurant);

        return ResponseEntity.ok(result);
    }

    //Dishes
    @PostMapping("/{id}/menu")
    public ResponseEntity<DishDto> createDish(@PathVariable final UUID id, @RequestBody DishDto dishDto){
        final RestaurantId restaurantId = new RestaurantId(id);

        Dish dish = restaurants.createDish(restaurantId, dishDto.name(), dishDto.description(), dishDto.price());

        return ResponseEntity.ok(DishDto.from(dish));
    }

    //GET
    //Restaurant
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> findById(@PathVariable final UUID id) {
        final RestaurantId restaurantId = new RestaurantId(id);
        final Restaurant restaurant = restaurants.findByIdWithMenuAndOpeningHours(restaurantId);
        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> findAll() {
        List<Restaurant> allRestaurants = restaurants.findAll();

        List<RestaurantDto> dtos = allRestaurants.stream()
                .map(RestaurantDto::from)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    //Dishes
    @GetMapping("/{id}/menu_full")
    public ResponseEntity<List<DishDto>> findFullMenu(@PathVariable final UUID id) {
        final RestaurantId restaurantId = new RestaurantId(id);
        List<Dish> allDishes = restaurants.findByIdWithMenu(restaurantId).getFullMenu();

        List<DishDto> dtos = allDishes.stream()
                .map(DishDto::from)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<DishDto>> findPublicMenu(@PathVariable final UUID id) {
        final RestaurantId restaurantId = new RestaurantId(id);
        List<Dish> allDishes = restaurants.findMenuOnDishState(restaurantId, DishState.PUBLISHED);

        List<DishDto> dtos = allDishes.stream()
                .map(DishDto::from)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/menu/{dishId}")
    public ResponseEntity<DishDto> findDish(@PathVariable final UUID id, @PathVariable final UUID dishId) {
        final RestaurantId restaurantId = new RestaurantId(id);
        final DishId dId = new DishId(dishId);

        Dish dish = restaurants.findByIdWithMenu(restaurantId).getDish(dId);

        return ResponseEntity.ok(DishDto.from(dish));
    }

    //PATCH
    //Restaurant
    @PatchMapping("/{id}")
    public ResponseEntity<RestaurantDto> openOrCloseRestaurant(@PathVariable final UUID id,
                                                               @RequestParam final boolean open){
        final RestaurantId restaurantId = new RestaurantId(id);

        Restaurant restaurant = restaurants.findByIdWithMenuAndOpeningHours(restaurantId);
        restaurant.open(open);

        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }

    @PatchMapping("/{id}/resetOverwrite")
    public ResponseEntity<RestaurantDto> stopOpeningHoursOverwrite(@PathVariable final UUID id){
        final RestaurantId restaurantId = new RestaurantId(id);

        Restaurant restaurant = restaurants.findByIdWithMenuAndOpeningHours(restaurantId);
        restaurant.stopOverwriteOpeningHours();

        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }

    //Dishes
    @PatchMapping("/{id}/menu/{dishId}")
    public ResponseEntity<DishDto> updateDish(@PathVariable final UUID id, @PathVariable final UUID dishId, @RequestBody DishDto dishDto){
        final RestaurantId restaurantId = new RestaurantId(id);
        final DishId dId = new DishId(dishId);

        Dish dish = restaurants.UpdateDish(restaurantId, dId, dishDto.name(), dishDto.description());

        return ResponseEntity.ok(DishDto.from(dish));
    }

    @PatchMapping("/{id}/menu/{dishId}/state")
    public ResponseEntity<DishDto> updateDishState(@PathVariable final UUID id, @PathVariable final UUID dishId, @RequestBody DishDto dishDto){
        final RestaurantId restaurantId = new RestaurantId(id);
        final DishId dId = new DishId(dishId);

        Dish dish = restaurants.UpdateDishState(restaurantId, dId, dishDto.state());

        return ResponseEntity.ok(DishDto.from(dish));
    }
}
