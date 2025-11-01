package be.kdg.sa.restaurantservice.infrastructure.jpa.restaurant;

import be.kdg.sa.restaurantservice.domain.restaurant.*;
import be.kdg.sa.restaurantservice.domain.restaurant.priceCriteria.MeanPriceCriteriaCalculator;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "restaurants")
public class JpaRestaurantEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID ownerId;

    @Column
    private String name;

    //Address is split in 4
    @Column
    private String street;

    @Column
    private int number;

    @Column
    private int postalCode;

    @Column
    private String country;

    @Column
    private String contactEmail;

    @Enumerated(value = EnumType.STRING)
    @Column
    private RestaurantType type;

    @OneToMany(mappedBy = "restaurant", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<JpaDishEntity> menu;

    @Column
    private String logo;

    @OneToMany(mappedBy = "restaurant", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<JpaRestaurantOpeningHours> openingHours;

    @Enumerated(value = EnumType.STRING)
    @Column
    private OverrideStatus overrideStatus;

    protected JpaRestaurantEntity() {
    }

    public JpaRestaurantEntity(UUID id, UUID ownerId, String name, String street, int number, int postalCode, String country, String contactEmail, RestaurantType type, String logo, OverrideStatus overrideStatus) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.street = street;
        this.number = number;
        this.postalCode = postalCode;
        this.country = country;
        this.contactEmail = contactEmail;
        this.type = type;
        this.logo = logo;
        this.overrideStatus = overrideStatus;
        this.menu = new ArrayList<>();
        this.openingHours = new ArrayList<>();
    }

    public static JpaRestaurantEntity fromDomain(Restaurant restaurant) {
        //Filling Jpa Object without the menu
        JpaRestaurantEntity jpaRestaurantEntity = new JpaRestaurantEntity(
                restaurant.getId().id(),
                restaurant.getOwnerId().id(),
                restaurant.getName(),
                restaurant.getAddress().street(),
                restaurant.getAddress().number(),
                restaurant.getAddress().postalCode(),
                restaurant.getAddress().country(),
                restaurant.getContactEmail(),
                restaurant.getType(),
                restaurant.getLogo(),
                restaurant.getOverrideStatus()
        );

        //Filling the Jpa Menu
        jpaRestaurantEntity.setMenu(
                restaurant.getFullMenu().stream()
                .map(JpaDishEntity::fromDomain)
                .toList()
        );

        //Filling the Jpa Opening Hours
        jpaRestaurantEntity.setOpeningHours(
                restaurant.getOpeningHours().stream()
                .map(JpaRestaurantOpeningHours::fromDomain)
                .toList()
        );

        return jpaRestaurantEntity;
    }

    public Restaurant toDomain() {
        return new Restaurant(
                new RestaurantId(id),
                new OwnerId(ownerId),
                name,
                new Address(street, number, postalCode, country),
                contactEmail,
                type,
                logo,
                openingHours.stream()
                        .map(JpaRestaurantOpeningHours::toDomain)
                        .collect(Collectors.toCollection(ArrayList::new)),
                menu.stream()
                        .map(JpaDishEntity::toDomain)
                        .collect(Collectors.toCollection(ArrayList::new)),
                overrideStatus,
                new MeanPriceCriteriaCalculator()
        );
    }

    public void setMenu(List<JpaDishEntity> menu) {
        this.menu = menu;
        this.menu.forEach(dish -> dish.setRestaurant(this));
    }

    public void setOpeningHours(List<JpaRestaurantOpeningHours> openingHours) {
        this.openingHours = openingHours;
        this.openingHours.forEach(roh -> roh.setRestaurant(this));
    }
}
