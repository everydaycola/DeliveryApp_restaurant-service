package be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa;

import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "dishes")
public class JpaDishEntity {

    @Id
    private UUID id;

    @Column
    private String name;

    @Column
    @Enumerated(value = EnumType.STRING)
    private DishState state;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private JpaRestaurantEntity restaurant;

    @Column
    private String description;

    protected JpaDishEntity(){}

    public JpaDishEntity(UUID id, String name, DishState state, String description) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.description = description;
    }

    static JpaDishEntity fromDomain(Dish dish){
        return new JpaDishEntity(
                dish.getId().id(),
                dish.getName(),
                dish.getState(),
                dish.getDescription()
        );
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public DishState getState() {
        return state;
    }

    public void setRestaurant(JpaRestaurantEntity restaurant) {
        this.restaurant = restaurant;
    }
}
