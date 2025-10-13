package be.kdg.sa.restaurantservice.api;

import be.kdg.sa.restaurantservice.TestHelper;
import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.dish.DishState;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RestaurantControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestHelper testHelper;

    //Happy Path
    @Test
    void shouldReturnTheMenuOfDishesWithAPublicState() throws Exception{
        //Arrange
        Restaurant restaurant = testHelper.saveRestaurant();
        testHelper.saveDish(restaurant.getId(), "Pasta Testo Public", DishState.PUBLISHED, "Testeken", 1.23);
        //Act & Assert
        mockMvc.perform(
                get("/api/restaurants/{id}/menu",restaurant.getId().id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        //Cleanup
        testHelper.cleanUp();
    }

    @Test
    void shouldReturnAnEmptyMenuWhenNoDishesArePublic() throws Exception{
        //Arrange
        Restaurant restaurant = testHelper.saveRestaurant();
        testHelper.saveDish(restaurant.getId(), "Pasta Testo Public", DishState.NOT_PUBLISHED, "Testeken", 1.23);
        //Act & Assert
        mockMvc.perform(
                        get("/api/restaurants/{id}/menu",restaurant.getId().id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        //Cleanup
        testHelper.cleanUp();
    }

    @Test
    void shouldChangeDishStateToPublished() throws Exception {
        //Arrange
        Restaurant restaurant = testHelper.saveRestaurant();
        Dish dish = restaurant.addDish("Pasta Testo", "Test Pasta", 1.23);
        testHelper.saveRestaurant(restaurant);
        //Act & Assert
        mockMvc.perform(
                        patch("/api/restaurants/{restaurantId}/menu/{dishId}/state", restaurant.getId().id(), dish.getId().id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"state\": \"PUBLISHED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("PUBLISHED"));
        //Cleanup
        testHelper.cleanUp();
    }
}