package be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface JpaRestaurantRepository extends JpaRepository<JpaRestaurantEntity, UUID> {

    @Query(value = """
            select r
            from JpaRestaurantEntity r
            left join JpaDishEntity d on d.restaurant.id = :id
            where r.id = :id
            """)
    Optional<JpaRestaurantEntity> findByIdWithMenu(UUID id);

    @Query(value = """
            select r
            from JpaRestaurantEntity r
            left join JpaDishEntity d on d.restaurant.id = :id
            left join JpaRestaurantOpeningHours roh on roh.restaurant.id = :id
            where r.id = :id
            """)
    Optional<JpaRestaurantEntity> findByIdWithMenuAndOpeningHours(UUID id);
}
