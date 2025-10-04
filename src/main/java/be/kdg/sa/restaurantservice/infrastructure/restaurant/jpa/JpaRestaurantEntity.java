package be.kdg.sa.restaurantservice.infrastructure.restaurant.jpa;

import be.kdg.sa.restaurantservice.domain.dish.DishId;
import be.kdg.sa.restaurantservice.domain.restaurant.*;
import jakarta.persistence.*;
import jakarta.websocket.ClientEndpoint;

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

    //TODO: Opening Hours Column

    protected JpaRestaurantEntity(){};

    public JpaRestaurantEntity(UUID id, UUID ownerId, String name, String street, int number, int postalCode, String country, String contactEmail, RestaurantType type, String logo) {
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
                restaurant.getLogo()
                );

        //Filling the Jpa Menu
        List<JpaDishEntity> jpaDishEntities = restaurant.getFullMenu().stream()
                .map(JpaDishEntity::fromDomain)
                .toList();
        jpaRestaurantEntity.setMenu(jpaDishEntities);

        return jpaRestaurantEntity;
    }

    public Restaurant toDomain(){
        Restaurant restaurant = new Restaurant(
                new RestaurantId(id),
                new OwnerId(id),
                name,
                new Address(street, number, postalCode, country),
                contactEmail,
                type,
                new ArrayList<>(),
                new PriceCriteria(){},
                logo
        );
        menu.forEach(jpaDish ->{
            restaurant.addDishFromRepository(jpaDish.getId(), jpaDish.getName(), jpaDish.getDescription(), jpaDish.getState());
        });

        return restaurant;
    }

    public void setMenu(List<JpaDishEntity> menu){
        this.menu = menu;
        this.menu.forEach(dish -> dish.setRestaurant(this));
    }

    public List<JpaDishEntity> getMenu() {
        return menu;
    }
}
