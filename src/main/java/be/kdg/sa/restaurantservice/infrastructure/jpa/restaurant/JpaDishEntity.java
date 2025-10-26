package be.kdg.sa.restaurantservice.infrastructure.jpa.restaurant;

import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "dishes")
public class JpaDishEntity {

    @Getter @Id
    private UUID id;

    @Getter @Column
    private String name;

    @Getter @Column
    @Enumerated(value = EnumType.STRING)
    private DishState state;

    @Setter @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private JpaRestaurantEntity restaurant;

    @Getter @Column
    private String description;

    @Getter @Column
    private double price;

    protected JpaDishEntity(){}

    public JpaDishEntity(UUID id, String name, DishState state, String description, double price) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.description = description;
        this.price = price;
    }

    static JpaDishEntity fromDomain(Dish dish){
        return new JpaDishEntity(
                dish.getId().id(),
                dish.getName(),
                dish.getState(),
                dish.getDescription(),
                dish.getPrice()
        );
    }

    public Dish toDomain(){
        return new Dish(
                new DishId(this.id),
                this.name,
                this.state,
                this.description,
                this.price
        );
    }

}
