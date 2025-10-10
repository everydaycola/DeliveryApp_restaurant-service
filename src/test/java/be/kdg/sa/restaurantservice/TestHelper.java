package be.kdg.sa.restaurantservice;

import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantId;
import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantType;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaDishEntity;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaRestaurantEntity;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaRestaurantOpeningHours;
import be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa.JpaRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Component
public class TestHelper {
    @Autowired
    private JpaRestaurantRepository jpaRestaurantRepository;

    public Restaurant saveRestaurant(){
        JpaRestaurantEntity restaurantEntity = new JpaRestaurantEntity(UUID.randomUUID(),UUID.randomUUID(),"Testaurant","Meir",10,2000,"Belgium", "test@email.com", RestaurantType.FASTFOOD, "Test.png", false, false);
        jpaRestaurantRepository.save(restaurantEntity);
        return restaurantEntity.toDomain();
    }

    public void saveDish(RestaurantId restaurantId, String name, DishState state, String description, double price ){
        JpaRestaurantEntity restaurantEntity = jpaRestaurantRepository.findByIdWithMenu(restaurantId.id()).orElseThrow(RuntimeException::new);
        JpaDishEntity dishEntity = new JpaDishEntity(UUID.randomUUID(), name, state, description, price);
        restaurantEntity.setMenu(List.of(dishEntity));
        jpaRestaurantRepository.save(restaurantEntity);
    }

    public void saveOpeningHours(RestaurantId restaurantId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime closingTime){
        JpaRestaurantEntity restaurantEntity = jpaRestaurantRepository.findById(restaurantId.id()).orElseThrow(RuntimeException::new);
        JpaRestaurantOpeningHours openingHours  = new JpaRestaurantOpeningHours(UUID.randomUUID(), dayOfWeek, startTime, closingTime);
        restaurantEntity.setOpeningHours(List.of(openingHours));
    }

    public void cleanUp(){
        jpaRestaurantRepository.deleteAll();
    }
}
