package be.kdg.sa.restaurantservice.infrastructure.jpa.restaurant;

import be.kdg.sa.restaurantservice.domain.restaurant.*;
import be.kdg.sa.restaurantservice.domain.restaurant.priceCriteria.MeanPriceCriteriaCalculator;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Column
    private boolean isOpen;

    @Column
    private boolean overwriteOpeningHours;

    protected JpaRestaurantEntity(){}

    public JpaRestaurantEntity(UUID id, UUID ownerId, String name, String street, int number, int postalCode, String country, String contactEmail, RestaurantType type, String logo, boolean isOpen, boolean overwriteOpeningHours) {
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
        this.isOpen = isOpen;
        this.overwriteOpeningHours = overwriteOpeningHours;
        this.menu = new ArrayList<>();
        this.openingHours = new ArrayList<>();
    }

    public static JpaRestaurantEntity fromDomain(Restaurant restaurant){
        //Filling Jpa Object without the menu
        JpaRestaurantEntity jpaRestaurantEntity = new JpaRestaurantEntity(
                restaurant.getId().id(),
                restaurant.getOwnerId().id(),
                restaurant.getName(),
                restaurant.getAddress().getStreet(),
                restaurant.getAddress().getNumber(),
                restaurant.getAddress().getPostalCode(),
                restaurant.getAddress().getCountry(),
                restaurant.getContactEmail(),
                restaurant.getType(),
                restaurant.getLogo(),
                restaurant.isOpen(),
                restaurant.isOverwriteOpeningHours()
                );

        //Filling the Jpa Menu
        List<JpaDishEntity> jpaDishEntities = restaurant.getFullMenu().stream()
                .map(JpaDishEntity::fromDomain)
                .toList();
        jpaRestaurantEntity.setMenu(jpaDishEntities);

        //Filling the Jpa Opening Hours
        List<JpaRestaurantOpeningHours> jpaRestaurantOpeningHours = restaurant.getOpeningHours().stream()
                .map(JpaRestaurantOpeningHours::fromDomain)
                .toList();
        jpaRestaurantEntity.setOpeningHours(jpaRestaurantOpeningHours);

        return jpaRestaurantEntity;
    }

    public Restaurant toDomain(){
        Restaurant restaurant = new Restaurant(
                new RestaurantId(id),
                new OwnerId(ownerId),
                name,
                new Address(street, number, postalCode, country),
                contactEmail,
                type,
                logo,
                isOpen,
                overwriteOpeningHours,
                new MeanPriceCriteriaCalculator()
        );
        menu.forEach(jpaDish ->
            restaurant.addDishFromRepository(jpaDish.getId(), jpaDish.getName(), jpaDish.getDescription(), jpaDish.getState(), jpaDish.getPrice()));

        openingHours.forEach(jpaRoh ->
                restaurant.addOpeningHours(jpaRoh.getDay(), jpaRoh.getOpeningTime(), jpaRoh.getClosingTime()));
        return restaurant;
    }

    public void setMenu(List<JpaDishEntity> menu){
        this.menu = menu;
        this.menu.forEach(dish -> dish.setRestaurant(this));
    }

    public void setOpeningHours(List<JpaRestaurantOpeningHours> openingHours){
        this.openingHours = openingHours;
        this.openingHours.forEach(roh -> roh.setRestaurant(this));
    }

    public List<JpaDishEntity> getMenu() {
        return menu;
    }

    public List<JpaRestaurantOpeningHours> getOpeningHours() {
        return openingHours;
    }
}
