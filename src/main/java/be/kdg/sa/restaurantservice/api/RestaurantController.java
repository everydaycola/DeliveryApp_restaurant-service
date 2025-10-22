package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.api.dtos.OrderMessagingDto;
import be.kdg.sa.restaurantservice.api.dtos.OrderDto;
import be.kdg.sa.restaurantservice.api.dtos.DishDto;
import be.kdg.sa.restaurantservice.api.dtos.DishScheduleDto;
import be.kdg.sa.restaurantservice.api.dtos.NewRestaurantDto;
import be.kdg.sa.restaurantservice.api.dtos.RestaurantDto;
import be.kdg.sa.restaurantservice.application.OrderService;
import be.kdg.sa.restaurantservice.application.RestaurantService;
import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.order.Order;
import be.kdg.sa.restaurantservice.domain.order.OrderId;
import be.kdg.sa.restaurantservice.domain.order.OrderStatus;
import be.kdg.sa.restaurantservice.domain.restaurant.OwnerId;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.RabbitMQTopology;
import be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages.OrderAcceptedMessage;
import be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages.OrderReadyMessage;
import be.kdg.sa.restaurantservice.infrastructure.rabbitMQ.messages.OrderRejectedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantService restaurants;
    private final OrderService orders;
    private final RabbitTemplate rabbitTemplate;

    public RestaurantController(RestaurantService restaurants, OrderService orders, RabbitTemplate rabbitTemplate) {
        this.restaurants = restaurants;
        this.orders = orders;
        this.rabbitTemplate = rabbitTemplate;
    }

    //POST
    //Restaurant
    @PostMapping
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<NewRestaurantDto> create(@RequestBody RestaurantDto restaurantDto,
                                                   @AuthenticationPrincipal Jwt token) {
        Restaurant restaurant = restaurants.create(
                new OwnerId(UUID.fromString(token.getClaimAsString("databaseid"))),
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

    @GetMapping("/{id}/orders_pending")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<List<OrderDto>> findAllPendingOrdersForRestaurant(@PathVariable UUID id,
                                                                            @AuthenticationPrincipal Jwt token){
        final RestaurantId restaurantId = new RestaurantId(id);

        final OwnerId ownerId = new OwnerId(UUID.fromString(token.getClaimAsString("databaseid")));
        restaurants.checkOwnership(restaurantId, ownerId);

        List<Order> pendingOrders = orders.findAllByRestaurantIdAndStatus(restaurantId, OrderStatus.PENDING);

        List<OrderDto> dtos = pendingOrders.stream().map(OrderDto::from).toList();

        return ResponseEntity.ok(dtos);
    }

    //Dishes
    @GetMapping("/{id}/menu_full")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<List<DishDto>> findFullMenu(@PathVariable final UUID id,
                                                      @AuthenticationPrincipal Jwt token) {
        final RestaurantId restaurantId = new RestaurantId(id);
        Restaurant restaurant = restaurants.findByIdWithMenu(restaurantId);
        final OwnerId ownerId = new OwnerId(UUID.fromString(token.getClaimAsString("databaseid")));
        restaurant.checkIfOwnerBy(ownerId);
        List<Dish> allDishes = restaurant.getFullMenu();

        List<DishDto> dtos = allDishes.stream()
                .map(DishDto::from)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<DishDto>> findPublicMenu(@PathVariable final UUID id) {
        final RestaurantId restaurantId = new RestaurantId(id);
        List<Dish> allDishes = restaurants.findMenuWithDishState(restaurantId, DishState.PUBLISHED);

        List<DishDto> dtos = allDishes.stream()
                .map(DishDto::from)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/menu/{dishId}")
    public ResponseEntity<DishDto> findDish(@PathVariable final UUID id, @PathVariable final UUID dishId) {
        final RestaurantId restaurantId = new RestaurantId(id);
        final DishId dId = new DishId(dishId);

        Dish dish = restaurants.findDishById(restaurantId, dId);

        return ResponseEntity.ok(DishDto.from(dish));
    }

    //PATCH
    //Restaurant
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<RestaurantDto> openOrCloseRestaurant(@PathVariable final UUID id,
                                                               @RequestParam final boolean open,
                                                               @AuthenticationPrincipal Jwt token){
        final RestaurantId restaurantId = new RestaurantId(id);
        final OwnerId ownerId = new OwnerId(UUID.fromString(token.getClaimAsString("databaseid")));

        Restaurant restaurant = restaurants.openOrCloseRestaurant(restaurantId, open, ownerId);

        return ResponseEntity.ok(RestaurantDto.from(restaurant));
    }

    @PatchMapping("/{id}/resetOverwrite")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity stopOpeningHoursOverwrite(@PathVariable final UUID id,
                                                    @AuthenticationPrincipal Jwt token){
        final RestaurantId restaurantId = new RestaurantId(id);
        final OwnerId ownerId = new OwnerId(UUID.fromString(token.getClaimAsString("databaseid")));

        restaurants.resetOverwrite(restaurantId, ownerId);

        return ResponseEntity.noContent().build();
    }

    //Dishes
    @PatchMapping("/{id}/menu/{dishId}")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<DishDto> updateDish(@PathVariable final UUID id,
                                              @PathVariable final UUID dishId,
                                              @RequestBody DishDto dishDto,
                                              @AuthenticationPrincipal Jwt token){
        final RestaurantId restaurantId = new RestaurantId(id);
        final DishId dId = new DishId(dishId);
        final OwnerId ownerId = new OwnerId(UUID.fromString(token.getClaimAsString("databaseid")));

        Dish dish = restaurants.updateDish(restaurantId, dId, dishDto.name(), dishDto.description(), ownerId);

        return ResponseEntity.ok(DishDto.from(dish));
    }

    @PatchMapping("/{id}/menu/{dishId}/state")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<DishDto> changeDishState(@PathVariable final UUID id,
                                                   @PathVariable final UUID dishId,
                                                   @RequestBody DishDto dishDto,
                                                   @AuthenticationPrincipal Jwt token){
        final RestaurantId restaurantId = new RestaurantId(id);
        final DishId dId = new DishId(dishId);
        final OwnerId ownerId = new OwnerId(UUID.fromString(token.getClaimAsString("databaseid")));

        Dish dish = restaurants.updateDishState(restaurantId, dId, dishDto.state(), ownerId);

        return ResponseEntity.ok(DishDto.from(dish));
    }

    @PatchMapping("/{id}/menu/publish_ready")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<List<DishDto>> publishReadyDishes(@PathVariable final UUID id,
                                                            @AuthenticationPrincipal Jwt token){
        final RestaurantId restaurantId = new RestaurantId(id);
        final OwnerId ownerId = new OwnerId(UUID.fromString(token.getClaimAsString("databaseid")));

        List<Dish> dishes = restaurants.publishReadyDishes(restaurantId, ownerId);

        List<DishDto> dtos = dishes.stream()
                .map(DishDto::from)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/menu/publish_schedule")
    @PreAuthorize("hasAuthority('owner')")
    public ResponseEntity<List<DishDto>> publishDishesOnSchedule(@PathVariable final UUID id,
                                                                 @RequestBody DishScheduleDto dishScheduleDto,
                                                                 @AuthenticationPrincipal Jwt token){
        final RestaurantId restaurantId = new RestaurantId(id);
        final OwnerId ownerId = new OwnerId(UUID.fromString(token.getClaimAsString("databaseid")));
        List<DishId> dishIds = dishScheduleDto.dishIds().stream().map(DishId::new).toList();

        List<Dish> updatedDishes = restaurants.publishDishesOnSchedule(restaurantId, dishScheduleDto.scheduledDate(), dishIds, ownerId);
        List<DishDto> dtos = updatedDishes.stream()
                .map(DishDto::from)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    //Messaging (RabbitMQ)
    @PatchMapping("/{id}/orders/{orderId}/accept")
    public ResponseEntity<OrderDto> acceptOrder(@PathVariable final UUID id, @PathVariable final UUID orderId){
        final RestaurantId restaurantId = new RestaurantId(id);
        final OrderId ordId = new OrderId(orderId);

        Order order = orders.acceptOrder(restaurantId,ordId, true);

        rabbitTemplate.convertAndSend(RabbitMQTopology.KDG_EXCHANGE_NAME,"order.accepted", new OrderAcceptedMessage(OrderMessagingDto.from(order)));

        return ResponseEntity.ok(OrderDto.from(order));
    }

    @PatchMapping("/{id}/orders/{orderId}/reject")
    public ResponseEntity<OrderDto> rejectOrder(@PathVariable final UUID id, @PathVariable final UUID orderId){
        final RestaurantId restaurantId = new RestaurantId(id);
        final OrderId ordId = new OrderId(orderId);

        Order order = orders.acceptOrder(restaurantId,ordId,false);

        rabbitTemplate.convertAndSend(RabbitMQTopology.KDG_EXCHANGE_NAME,"order.rejected", new OrderRejectedMessage(OrderMessagingDto.from(order)));

        return ResponseEntity.ok(OrderDto.from(order));
    }

    @PatchMapping("/{id}/orders/{orderId}/ready")
    public ResponseEntity<OrderDto> readyOrder(@PathVariable final UUID id, @PathVariable final UUID orderId){
        final RestaurantId restaurantId = new RestaurantId(id);
        final OrderId ordId = new OrderId(orderId);

        Order order = orders.readyOrder(restaurantId,ordId);

        rabbitTemplate.convertAndSend(RabbitMQTopology.KDG_EXCHANGE_NAME,"order.ready", new OrderReadyMessage(OrderMessagingDto.from(order)));

        return ResponseEntity.ok(OrderDto.from(order));
    }
}
