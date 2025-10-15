package be.kdg.sa.restaurantservice.infrastructure.jpa;

import be.kdg.sa.restaurantservice.domain.restaurant.RestaurantOpeningHours;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

//This is only an entity in the Database
@Entity
@Table(name = "openingHours")
public class JpaRestaurantOpeningHours {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private JpaRestaurantEntity restaurant;

    @Column
    @Enumerated(value = EnumType.STRING)
    private DayOfWeek day;

    @Column
    private LocalTime openingTime;

    @Column
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

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setRestaurant(JpaRestaurantEntity restaurant) {
        this.restaurant = restaurant;
    }
}
