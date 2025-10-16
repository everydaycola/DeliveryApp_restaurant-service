package be.kdg.sa.restaurantservice.infrastructure.jpa.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<JpaOrderEntity, UUID> {

}
