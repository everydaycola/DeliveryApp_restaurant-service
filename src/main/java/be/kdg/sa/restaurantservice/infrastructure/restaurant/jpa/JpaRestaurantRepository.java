package be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaRestaurantRepository extends JpaRepository<JpaRestaurantEntity, UUID> {

    @Query(value = """
            select r 
            from JpaRestaurantEntity r
            left join fetch r.menu where r.id = :id
            """)
    Optional<JpaRestaurantEntity> findByIdWithMenu(UUID id);
}
