package be.kdg.sa.restaurantservice;

import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.*;
import be.kdg.sa.restaurantservice.infrastructure.jpa.restaurant.JpaDishEntity;
import be.kdg.sa.restaurantservice.infrastructure.jpa.restaurant.JpaRestaurantEntity;
import be.kdg.sa.restaurantservice.infrastructure.jpa.restaurant.JpaRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class TestHelper {
    @Autowired
    private JpaRestaurantRepository jpaRestaurantRepository;

    private static final String DATABASEID = "databaseid";

    public Restaurant saveRestaurant(UUID ownerId){
        final var restaurantEntity = new JpaRestaurantEntity(UUID.randomUUID(),ownerId,"Testaurant","Meir",10,2000,"Belgium", "test@email.com", RestaurantType.FASTFOOD, "Test.png", OverrideStatus.NONE);
        jpaRestaurantRepository.save(restaurantEntity);
        return restaurantEntity.toDomain();
    }

    public void saveRestaurant(Restaurant restaurant){
        jpaRestaurantRepository.save(JpaRestaurantEntity.fromDomain(restaurant));
    }

    public void saveDish(RestaurantId restaurantId, String name, DishState state, String description, double price ){
        final var restaurantEntity = jpaRestaurantRepository.findByIdWithMenu(restaurantId.id()).orElseThrow(RuntimeException::new);
        final var dishEntity = new JpaDishEntity(UUID.randomUUID(), name, state, description, price);
        restaurantEntity.setMenu(List.of(dishEntity));
        jpaRestaurantRepository.save(restaurantEntity);
    }

    public JpaDishEntity createDish(String name, DishState state, String description, double price){
        return new JpaDishEntity(UUID.randomUUID(), name, state, description, price);
    }

    public void SaveDishes(RestaurantId restaurantId, List<JpaDishEntity> dishes){
        final var restaurantEntity = jpaRestaurantRepository.findByIdWithMenu(restaurantId.id()).orElseThrow(RuntimeException::new);
        restaurantEntity.setMenu(dishes);
        jpaRestaurantRepository.save(restaurantEntity);
    }

    public void cleanUp(){
        jpaRestaurantRepository.deleteAll();
    }

    public JwtAuthenticationToken getJwtToken(UUID ownerId){
        final var jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "user")
                .claim(DATABASEID, ownerId.toString())
                .claim("authorities", Collections.singletonList("owner"))
                .build();
        return new JwtAuthenticationToken(
                jwt,
                Collections.singletonList(new SimpleGrantedAuthority("owner"))
        );

    }
}
