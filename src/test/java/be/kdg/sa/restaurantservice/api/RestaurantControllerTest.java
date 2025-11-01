package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.TestHelper;
import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestHelper testHelper;

    private static final UUID OwnerId = UUID.randomUUID();

    //GET Full menu
    @Test
    void shouldReturnAllDishesNoMatterTheState() throws Exception {
        //Arrange
        final var restaurant = testHelper.saveRestaurant(OwnerId);
        testHelper.SaveDishes(restaurant.getId(),
                List.of(
                        testHelper.createDish("Pasta Testo", DishState.NOT_PUBLISHED, "Test Pasta", 1.23),
                        testHelper.createDish("Spaghetti Testo", DishState.READY_FOR_PUBLISHING, "Test spaghetti", 12.3),
                        testHelper.createDish("Penne Testo rossa", DishState.PUBLISHED, "Test Penno but read", 32.1)
                ));

        final var token = testHelper.getJwtToken(OwnerId);
        //Act & Assert
        mockMvc.perform(
                        get("/api/restaurants/{id}/menu_full", restaurant.getId().id())
                                .with(authentication(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

        //Cleanup
        testHelper.cleanUp();
    }
    @Test
    void fullMenuShouldReturnUnauthorisedWhenNotLoggedIn() throws Exception {
        //Arrange
        final var restaurant = testHelper.saveRestaurant(OwnerId);
        testHelper.SaveDishes(restaurant.getId(),
                List.of(
                        testHelper.createDish("Pasta Testo", DishState.NOT_PUBLISHED, "Test Pasta", 1.23),
                        testHelper.createDish("Spaghetti Testo", DishState.READY_FOR_PUBLISHING, "Test spaghetti", 12.3),
                        testHelper.createDish("Penne Testo rossa", DishState.PUBLISHED, "Test Penno but read", 32.1)
                ));

        //Act & Assert
        mockMvc.perform(
                        get("/api/restaurants/{id}/menu_full", restaurant.getId().id()))
                .andExpect(status().isUnauthorized());

        //Cleanup
        testHelper.cleanUp();
    }

    //GET Menu
    //Happy Path
    @Test
    void shouldReturnTheMenuOfDishesWithAPublicState() throws Exception {
        //Arrange
        final var restaurant = testHelper.saveRestaurant(OwnerId);
        testHelper.saveDish(restaurant.getId(), "Pasta Testo", DishState.PUBLISHED, "Test Pasta", 1.23);
        //Act & Assert
        mockMvc.perform(
                        get("/api/restaurants/{id}/menu", restaurant.getId().id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        //Cleanup
        testHelper.cleanUp();
    }

    @Test
    void shouldReturnAnEmptyMenuWhenNoDishesArePublic() throws Exception {
        //Arrange
        final var restaurant = testHelper.saveRestaurant(OwnerId);
        testHelper.saveDish(restaurant.getId(), "Pasta Testo", DishState.NOT_PUBLISHED, "Test Pasta", 1.23);
        //Act & Assert
        mockMvc.perform(
                        get("/api/restaurants/{id}/menu", restaurant.getId().id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        //Cleanup
        testHelper.cleanUp();
    }

    //PATCH DishState
    //Happy Path
    @Test
    void shouldChangeDishStateToPublished() throws Exception {
        //Arrange
        final var restaurant = testHelper.saveRestaurant(OwnerId);
        final var dish = restaurant.addDish("Pasta Testo", "Test Pasta", 1.23);
        testHelper.saveRestaurant(restaurant);
        final var token = testHelper.getJwtToken(OwnerId);
        //Act & Assert
        mockMvc.perform(
                        patch("/api/restaurants/{restaurantId}/menu/{dishId}/state", restaurant.getId().id(), dish.getId().id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"state\": \"PUBLISHED\"}")
                                .with(authentication(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("PUBLISHED"));
        //Cleanup
        testHelper.cleanUp();
    }

    @Test
    void DishStateShouldReturnUnauthorisedWhenNotLoggedIn() throws Exception {
        //Arrange
        final var restaurant = testHelper.saveRestaurant(OwnerId);
        final var dish = restaurant.addDish("Pasta Testo", "Test Pasta", 1.23);
        testHelper.saveRestaurant(restaurant);
        //Act & Assert
        mockMvc.perform(
                        patch("/api/restaurants/{restaurantId}/menu/{dishId}/state", restaurant.getId().id(), dish.getId().id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"state\": \"PUBLISHED\"}"))
                .andExpect(status().isUnauthorized());
        //Cleanup
        testHelper.cleanUp();
    }

    @Test
    void shouldNotChangeDishStateWhenAnIncompatibleStateIsGiven() throws Exception {
        //Arrange
        final var restaurant = testHelper.saveRestaurant(OwnerId);
        final var dish = restaurant.addDish("Pasta Testo", "Test Pasta", 1.23);
        testHelper.saveRestaurant(restaurant);
        final var token = testHelper.getJwtToken(OwnerId);
        //Act & Assert
        mockMvc.perform(
                        patch("/api/restaurants/{restaurantId}/menu/{dishId}/state", restaurant.getId().id(), dish.getId().id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"state\": \"ON_FIRE\"}")
                                .with(authentication(token)))
                .andExpect(status().isBadRequest());
        //Cleanup
        testHelper.cleanUp();
    }

    //POST restaurant
    @Test
    void shouldAddANewRestaurant() throws Exception {
        //Arrange
        //Address json
        final var jsonAddress = new JSONObject();
        jsonAddress.put("street", "The High Road");
        jsonAddress.put("number", 27);
        jsonAddress.put("postalCode", 713);
        jsonAddress.put("country", "America");

        //OpeningHours json
        final var jsonOpeningHours = new JSONArray();

        final var monday = new JSONObject();
        monday.put("day", DayOfWeek.MONDAY.toString());
        monday.put("openingTime", "10:00:00");
        monday.put("closingTime", "22:00:00");

        final var tuesday = new JSONObject();
        tuesday.put("day", DayOfWeek.TUESDAY.toString());
        tuesday.put("openingTime", "10:00:00");
        tuesday.put("closingTime", "22:00:00");

        final var wednesday = new JSONObject();
        wednesday.put("day", DayOfWeek.WEDNESDAY.toString());
        wednesday.put("openingTime", "12:00:00");
        wednesday.put("closingTime", "20:00:00");

        jsonOpeningHours.put(monday);
        jsonOpeningHours.put(tuesday);
        jsonOpeningHours.put(wednesday);

        //Restaurant Json
        final var jsonRestaurant = new JSONObject();
        jsonRestaurant.put("name", "The Good, the Bread and Hungry");
        jsonRestaurant.put("address", jsonAddress);
        jsonRestaurant.put("contactEmail", "FlintEastcook@email.com");
        jsonRestaurant.put("restaurantType", "AMERICAN");
        jsonRestaurant.put("openingHours", jsonOpeningHours);
        jsonRestaurant.put("logo", "revolver.png");

        final var token = testHelper.getJwtToken(OwnerId);

        //Act & Assert
        mockMvc.perform(
                        post("/api/restaurants")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRestaurant.toString())
                                .with(authentication(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(jsonRestaurant.get("name")));

        //Cleanup
        testHelper.cleanUp();
    }

    @Test
    void ShouldReturnBadRequestWhenOwnerAlreadyHasARestaurant() throws Exception {
        //Arrange
        testHelper.saveRestaurant(OwnerId);

        //Address json
        final var jsonAddress = new JSONObject();
        jsonAddress.put("street", "The High Road");
        jsonAddress.put("number", 27);
        jsonAddress.put("postalCode", 713);
        jsonAddress.put("country", "America");

        //OpeningHours json
        final var jsonOpeningHours = new JSONArray();

        final var monday = new JSONObject();
        monday.put("day", DayOfWeek.MONDAY.toString());
        monday.put("openingTime", "10:00:00");
        monday.put("closingTime", "22:00:00");

        final var tuesday = new JSONObject();
        tuesday.put("day", DayOfWeek.TUESDAY.toString());
        tuesday.put("openingTime", "10:00:00");
        tuesday.put("closingTime", "22:00:00");

        final var wednesday = new JSONObject();
        wednesday.put("day", DayOfWeek.WEDNESDAY.toString());
        wednesday.put("openingTime", "12:00:00");
        wednesday.put("closingTime", "20:00:00");

        jsonOpeningHours.put(monday);
        jsonOpeningHours.put(tuesday);
        jsonOpeningHours.put(wednesday);

        //Restaurant Json
        final var jsonRestaurant = new JSONObject();
        jsonRestaurant.put("name", "The Good, the Bread and Hungry");
        jsonRestaurant.put("address", jsonAddress);
        jsonRestaurant.put("contactEmail", "FlintEastcook@email.com");
        jsonRestaurant.put("restaurantType", "AMERICAN");
        jsonRestaurant.put("openingHours", jsonOpeningHours);
        jsonRestaurant.put("logo", "revolver.png");

        final var token = testHelper.getJwtToken(OwnerId);

        //Act & Assert
        mockMvc.perform(
                        post("/api/restaurants")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRestaurant.toString())
                                .with(authentication(token)))
                .andExpect(status().isBadRequest());

        //Cleanup
        testHelper.cleanUp();
    }

    @Test
    void createRestaurantShouldReturnUnauthorisedWhenNotLoggedIn() throws Exception {
        //Arrange
        testHelper.saveRestaurant(OwnerId);

        //Address json
        final var jsonAddress = new JSONObject();
        jsonAddress.put("street", "The High Road");
        jsonAddress.put("number", 27);
        jsonAddress.put("postalCode", 713);
        jsonAddress.put("country", "America");

        //OpeningHours json
        final var jsonOpeningHours = new JSONArray();

        final var monday = new JSONObject();
        monday.put("day", DayOfWeek.MONDAY.toString());
        monday.put("openingTime", "10:00:00");
        monday.put("closingTime", "22:00:00");

        final var tuesday = new JSONObject();
        tuesday.put("day", DayOfWeek.TUESDAY.toString());
        tuesday.put("openingTime", "10:00:00");
        tuesday.put("closingTime", "22:00:00");

        final var wednesday = new JSONObject();
        wednesday.put("day", DayOfWeek.WEDNESDAY.toString());
        wednesday.put("openingTime", "12:00:00");
        wednesday.put("closingTime", "20:00:00");

        jsonOpeningHours.put(monday);
        jsonOpeningHours.put(tuesday);
        jsonOpeningHours.put(wednesday);

        //Restaurant Json
        final var jsonRestaurant = new JSONObject();
        jsonRestaurant.put("name", "The Good, the Bread and Hungry");
        jsonRestaurant.put("address", jsonAddress);
        jsonRestaurant.put("contactEmail", "FlintEastcook@email.com");
        jsonRestaurant.put("restaurantType", "AMERICAN");
        jsonRestaurant.put("openingHours", jsonOpeningHours);
        jsonRestaurant.put("logo", "revolver.png");

        //Act & Assert
        mockMvc.perform(
                        post("/api/restaurants")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRestaurant.toString()))
                .andExpect(status().isUnauthorized());

        //Cleanup
        testHelper.cleanUp();
    }

    @Test
    void shouldReturnBadRequestWhenEmptyBodyGivenWhenCreatingRestaurant() throws Exception {
        //Arrange
        final var restaurant = "";

        //Act & Assert
        mockMvc.perform(
                        post("/api/restaurants")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(restaurant))
                .andExpect(status().isBadRequest());

        //Cleanup
        testHelper.cleanUp();
    }

    //PATCH publish ready
    // Happy path
    @Test
    void shouldPublishAllDishesMarkedReadyForPublishing() throws Exception {
        //Arrange
        final var restaurant = testHelper.saveRestaurant(OwnerId);
        testHelper.SaveDishes(restaurant.getId(),
                List.of(
                        testHelper.createDish("Pasta Testo", DishState.READY_FOR_PUBLISHING, "Test Pasta", 1.23),
                        testHelper.createDish("Spaghetti Testo", DishState.READY_FOR_PUBLISHING, "Test spaghetti", 12.3),
                        testHelper.createDish("Penne Testo rossa", DishState.NOT_PUBLISHED, "Test Penno but read", 32.1)
                ));

        final var token = testHelper.getJwtToken(OwnerId);
        //Act & Assert
        mockMvc.perform(
                        patch("/api/restaurants/{id}/menu/publish_ready", restaurant.getId().id())
                                .with(authentication(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        //Cleanup
        testHelper.cleanUp();
    }

    @Test
    void publishReadyShouldReturnUnauthorisedWhenNotLoggedIn() throws Exception {
        //Arrange
        final var restaurant = testHelper.saveRestaurant(OwnerId);
        testHelper.SaveDishes(restaurant.getId(),
                List.of(
                        testHelper.createDish("Pasta Testo", DishState.READY_FOR_PUBLISHING, "Test Pasta", 1.23),
                        testHelper.createDish("Spaghetti Testo", DishState.READY_FOR_PUBLISHING, "Test spaghetti", 12.3),
                        testHelper.createDish("Penne Testo rossa", DishState.NOT_PUBLISHED, "Test Penno but read", 32.1)
                ));

        //Act & Assert
        mockMvc.perform(
                        patch("/api/restaurants/{id}/menu/publish_ready", restaurant.getId().id()))
                .andExpect(status().isUnauthorized());

        //Cleanup
        testHelper.cleanUp();
    }

    @Test
    void shouldReturnNothingWhenNoDishesReadyForPublishing() throws Exception {
        //Arrange
        final var restaurant = testHelper.saveRestaurant(OwnerId);
        testHelper.saveDish(restaurant.getId(),"Penne Testo rossa", DishState.NOT_PUBLISHED, "Test Penno but read", 32.1);

        final var token = testHelper.getJwtToken(OwnerId);
        //Act & Assert
        mockMvc.perform(
                        patch("/api/restaurants/{id}/menu/publish_ready", restaurant.getId().id())
                                .with(authentication(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        //Cleanup
        testHelper.cleanUp();
    }

    //PATCH
    // happy path
    @Test
    void shouldReturnTheUpdatedDish() throws Exception {
        //Arrange
        final var restaurant = testHelper.saveRestaurant(OwnerId);
        final var dish = restaurant.addDish("Pasta Testo", "Test Pasta", 1.23);
        testHelper.saveRestaurant(restaurant);

        final var updatedDish = new JSONObject();
        final var newName = "Penne Testo rossa (vernieuwd recept)";
        final var newDesc = "nu met een nieuw recept zonder dat er iets verloren gaat";
        updatedDish.put("name", newName);
        updatedDish.put("description", newDesc);

        final var token = testHelper.getJwtToken(OwnerId);
        //Act & Assert
        mockMvc.perform(
                        patch("/api/restaurants/{restaurantId}/menu/{dishId}", restaurant.getId().id(), dish.getId().id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatedDish.toString())
                                .with(authentication(token))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.description").value(newDesc));

        //Cleanup
        testHelper.cleanUp();
    }

    @Test
    void shouldThrowBadRequestWhenNoBody() throws Exception {
        //Arrange
        final var restaurant = testHelper.saveRestaurant(OwnerId);
        final var dish = restaurant.addDish("Pasta Testo", "Test Pasta", 1.23);
        testHelper.saveRestaurant(restaurant);

        final var token = testHelper.getJwtToken(OwnerId);
        //Act & Assert
        mockMvc.perform(
                        patch("/api/restaurants/{restaurantId}/menu/{dishId}/state", restaurant.getId().id(), dish.getId().id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(authentication(token))
                )
                .andExpect(status().isBadRequest());

        //Cleanup
        testHelper.cleanUp();
    }

    @Test
    void updateStateShouldReturnUnauthorizedWhenNotLoggedIn() throws Exception {
        //Arrange
        final var restaurant = testHelper.saveRestaurant(OwnerId);
        final var dish = restaurant.addDish("Pasta Testo", "Test Pasta", 1.23);
        testHelper.saveRestaurant(restaurant);

        final var updatedDish = new JSONObject();
        final var newName = "Penne Testo rossa (vernieuwd recept)";
        final var newDesc = "nu met een nieuw recept zonder dat er iets verloren gaat";
        updatedDish.put("name", newName);
        updatedDish.put("description", newDesc);
        //Act & Assert
        mockMvc.perform(
                        patch("/api/restaurants/{restaurantId}/menu/{dishId}/state", restaurant.getId().id(), dish.getId().id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatedDish.toString())
                )
                .andExpect(status().isUnauthorized());

        //Cleanup
        testHelper.cleanUp();
    }
}