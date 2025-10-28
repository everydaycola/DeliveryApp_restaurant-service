package be.kdg.sa.restaurantservice.infrastructure.jpa.restaurant;

import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantOpeningHours;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

//This is only an entity in the Database
@Entity
@Table(name = "openingHours")
public class JpaRestaurantOpeningHours {

    @Id
    private UUID id;

    @Setter @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private JpaRestaurantEntity restaurant;

    @Getter @Column
    @Enumerated(value = EnumType.STRING)
    private DayOfWeek day;

    @Getter @Column
    private LocalTime openingTime;

    @Getter @Column
    private LocalTime closingTime;

    protected JpaRestaurantOpeningHours() {}

    public JpaRestaurantOpeningHours(UUID id, DayOfWeek day, LocalTime openingTime, LocalTime closingTime) {
        this.id = id;
        this.day = day;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public static JpaRestaurantOpeningHours fromDomain(RestaurantOpeningHours roh){
        return new JpaRestaurantOpeningHours(UUID.randomUUID(), roh.getDay(), roh.getOpeningTime(), roh.getClosingTime());
    }

    public RestaurantOpeningHours toDomain() {
        return new RestaurantOpeningHours(day, openingTime, closingTime);
    }

}
