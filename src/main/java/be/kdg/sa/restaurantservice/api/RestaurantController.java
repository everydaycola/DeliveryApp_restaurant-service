package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.api.dtos.order.OrderDto;
import be.kdg.sa.restaurantservice.api.dtos.order.OrderMessagingDto;
import be.kdg.sa.restaurantservice.api.dtos.restaurant.NewRestaurantDto;
import be.kdg.sa.restaurantservice.api.dtos.restaurant.RestaurantDto;
import be.kdg.sa.restaurantservice.api.dtos.restaurant.dish.*;
import be.kdg.sa.restaurantservice.application.OrderService;
import be.kdg.sa.restaurantservice.application.RestaurantService;
import be.kdg.sa.restaurantservice.config.RabbitMQProperties;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.order.OrderId;
import be.kdg.sa.restaurantservice.domain.order.OrderStatus;
import be.kdg.sa.restaurantservice.domain.restaurant.OwnerId;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages.OrderAcceptedMessage;
import be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages.OrderReadyMessage;
import be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages.OrderRejectedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantService restaurants;
    private final OrderService orders;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;
    private static final String DATABASEID = "databaseid";

    public RestaurantController(RestaurantService restaurants, OrderService orders, RabbitTemplate rabbitTemplate,
                                RabbitMQProperties rabbitMQProperties) {
        this.restaurants = restaurants;
        this.orders = orders;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQProperties = rabbitMQProperties;
    }

    //POST
    //Restaurant
    @PostMapping
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<RestaurantDto> create(@RequestBody NewRestaurantDto newRestaurantDto,
                                                @AuthenticationPrincipal Jwt token) {
        log.info("Creating new restaurant {}", newRestaurantDto.name());
        final var restaurant = restaurants.create(
                new OwnerId(UUID.fromString(token.getClaimAsString(DATABASEID))),
                newRestaurantDto
        );

        final var result = RestaurantDto.from(restaurant);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<RestaurantDto> findByOwnerId(@AuthenticationPrincipal Jwt token) {
        log.info("Getting restaurant for owner {}", token.getClaimAsString(DATABASEID));
        final var ownerId = new OwnerId(UUID.fromString(token.getClaimAsString(DATABASEID)));
        final var restaurant = restaurants.findByOwnerId(ownerId);
        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }

    //Dishes
    @PostMapping("/{id}/menu")
    public ResponseEntity<DishDto> createDish(@PathVariable final UUID id, @RequestBody NewDishDto newDishDto){
        log.info("Creating new dish for restaurant {}", id);
        final var restaurantId = new RestaurantId(id);

        final var dish = restaurants.createDish(restaurantId, newDishDto.name(), newDishDto.description(), newDishDto.price());

        return ResponseEntity.ok(DishDto.from(dish));
    }

    //GET
    //Restaurant
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> findById(@PathVariable final UUID id) {
        log.info("Getting restaurant {}", id);
        final var restaurantId = new RestaurantId(id);
        final var restaurant = restaurants.findByIdWithMenuAndOpeningHours(restaurantId);
        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> findAll() {
        log.info("Getting all restaurants");
        final var allRestaurants = restaurants.findAll();

        final var dtos = allRestaurants.stream()
                .map(RestaurantDto::from)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/orders_pending")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<List<OrderDto>> findAllPendingOrdersForRestaurant(@PathVariable UUID id,
                                                                            @AuthenticationPrincipal Jwt token){
        log.info("Getting all pending orders for restaurant {}", id);
        final var restaurantId = new RestaurantId(id);

        final var ownerId = new OwnerId(UUID.fromString(token.getClaimAsString(DATABASEID)));
        restaurants.checkOwnership(restaurantId, ownerId);

        final var pendingOrders = orders.findAllByRestaurantIdAndStatus(restaurantId, OrderStatus.PENDING);

        final var dtos = pendingOrders.stream().map(OrderDto::from).toList();

        return ResponseEntity.ok(dtos);
    }

    //Dishes
    @GetMapping("/{id}/menu_full")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<List<DishDto>> findFullMenu(@PathVariable final UUID id,
                                                      @AuthenticationPrincipal Jwt token) {
        log.info("Getting full menu for restaurant {}", id);
        final var restaurantId = new RestaurantId(id);
        final var restaurant = restaurants.findByIdWithMenu(restaurantId);
        final var ownerId = new OwnerId(UUID.fromString(token.getClaimAsString(DATABASEID)));
        restaurant.checkIfOwnerBy(ownerId);
        final var allDishes = restaurant.getFullMenu();

        final var dtos = allDishes.stream()
                .map(DishDto::from)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<DishDto>> findPublicMenu(@PathVariable final UUID id) {
        log.info("Getting public menu for restaurant {}", id);
        final var restaurantId = new RestaurantId(id);
        final var allDishes = restaurants.findMenuWithDishState(restaurantId, DishState.PUBLISHED);

        final var dtos = allDishes.stream()
                .map(DishDto::from)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/menu/{dishId}")
    public ResponseEntity<DishDto> findDish(@PathVariable final UUID id, @PathVariable final UUID dishId) {
        log.info("Getting dish {} for restaurant {}", dishId, id);
        final var restaurantId = new RestaurantId(id);
        final var dId = new DishId(dishId);

        final var dish = restaurants.findDishById(restaurantId, dId);

        return ResponseEntity.ok(DishDto.from(dish));
    }

    //PATCH
    //Restaurant
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<RestaurantDto> openOrCloseRestaurant(@PathVariable final UUID id,
                                                               @RequestParam final boolean open,
                                                               @AuthenticationPrincipal Jwt token){
        log.info("Updating restaurant {} to {}", id, open);
        final var restaurantId = new RestaurantId(id);
        final var ownerId = new OwnerId(UUID.fromString(token.getClaimAsString(DATABASEID)));

        final var restaurant = restaurants.openOrCloseRestaurant(restaurantId, open, ownerId);

        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }

    @PatchMapping("/{id}/resetOverwrite")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<RestaurantDto> stopOpeningHoursOverwrite(@PathVariable final UUID id,
                                                    @AuthenticationPrincipal Jwt token){
        log.info("Stopping overwrite for restaurant {}", id);
        final var restaurantId = new RestaurantId(id);
        final var ownerId = new OwnerId(UUID.fromString(token.getClaimAsString(DATABASEID)));

        final var restaurant = restaurants.resetOverwrite(restaurantId, ownerId);

        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }

    //Dishes
    @PatchMapping("/{id}/menu/{dishId}")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<DishDto> updateDish(@PathVariable final UUID id,
                                              @PathVariable final UUID dishId,
                                              @RequestBody UpdateDishDto dishDto,
                                              @AuthenticationPrincipal Jwt token){
        log.info("Updating dish {} for restaurant {}", dishId, id);
        final var restaurantId = new RestaurantId(id);
        final var dId = new DishId(dishId);
        final var ownerId = new OwnerId(UUID.fromString(token.getClaimAsString(DATABASEID)));

        final var dish = restaurants.updateDish(restaurantId, dId, dishDto.name(), dishDto.description(), ownerId);

        return ResponseEntity.ok(DishDto.from(dish));
    }

    @PatchMapping("/{id}/menu/{dishId}/state")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<DishDto> changeDishState(@PathVariable final UUID id,
                                                   @PathVariable final UUID dishId,
                                                   @RequestBody UpdateDishStateDto dishStateDto,
                                                   @AuthenticationPrincipal Jwt token){
        log.info("Updating dish {} for restaurant {} to {}", dishId, id, dishStateDto.state());
        final var restaurantId = new RestaurantId(id);
        final var dId = new DishId(dishId);
        final var ownerId = new OwnerId(UUID.fromString(token.getClaimAsString(DATABASEID)));

        final var dish = restaurants.updateDishState(restaurantId, dId, dishStateDto.state(), ownerId);

        return ResponseEntity.ok(DishDto.from(dish));
    }

    @PatchMapping("/{id}/menu/publish_ready")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<List<DishDto>> publishReadyDishes(@PathVariable final UUID id,
                                                            @AuthenticationPrincipal Jwt token){
        final var restaurantId = new RestaurantId(id);
        final var ownerId = new OwnerId(UUID.fromString(token.getClaimAsString(DATABASEID)));

        final var dishes = restaurants.publishReadyDishes(restaurantId, ownerId);

        final var dtos = dishes.stream()
                .map(DishDto::from)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/menu/publish_schedule")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<List<DishDto>> publishDishesOnSchedule(@PathVariable final UUID id,
                                                                 @RequestBody DishScheduleDto dishScheduleDto,
                                                                 @AuthenticationPrincipal Jwt token){
        final var restaurantId = new RestaurantId(id);
        final var ownerId = new OwnerId(UUID.fromString(token.getClaimAsString(DATABASEID)));
        final var dishIds = dishScheduleDto.dishIds().stream().map(DishId::new).toList();

        final var updatedDishes = restaurants.publishDishesOnSchedule(restaurantId, dishScheduleDto.scheduledDate(), dishIds, ownerId);
        final var dtos = updatedDishes.stream()
                .map(DishDto::from)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    //Messaging (RabbitMQ)
    @PatchMapping("/{id}/orders/{orderId}/accept")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<OrderDto> acceptOrder(@PathVariable final UUID id, @PathVariable final UUID orderId){
        final var restaurantId = new RestaurantId(id);
        final var ordId = new OrderId(orderId);

        final var order = orders.acceptOrder(restaurantId,ordId, true);

        rabbitTemplate.convertAndSend(
                rabbitMQProperties.getExchangeName(),
                rabbitMQProperties.getOrderAcceptedBinding(),
                new OrderAcceptedMessage(OrderMessagingDto.from(order))
        );

        return ResponseEntity.ok(OrderDto.from(order));
    }

    @PatchMapping("/{id}/orders/{orderId}/reject")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<OrderDto> rejectOrder(@PathVariable final UUID id, @PathVariable final UUID orderId){
        final var restaurantId = new RestaurantId(id);
        final var ordId = new OrderId(orderId);

        final var order = orders.acceptOrder(restaurantId,ordId,false);

        rabbitTemplate.convertAndSend(
                rabbitMQProperties.getExchangeName(),
                rabbitMQProperties.getOrderRejectedBinding(),
                new OrderRejectedMessage(OrderMessagingDto.from(order))
        );

        return ResponseEntity.ok(OrderDto.from(order));
    }

    @PatchMapping("/{id}/orders/{orderId}/ready")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<OrderDto> readyOrder(@PathVariable final UUID id, @PathVariable final UUID orderId){
        final var restaurantId = new RestaurantId(id);
        final var ordId = new OrderId(orderId);

        final var order = orders.readyOrder(restaurantId,ordId);

        rabbitTemplate.convertAndSend(
                rabbitMQProperties.getExchangeName(),
                rabbitMQProperties.getOrderReadyBinding(),
                new OrderReadyMessage(OrderMessagingDto.from(order))
        );

        return ResponseEntity.ok(OrderDto.from(order));
    }
}
