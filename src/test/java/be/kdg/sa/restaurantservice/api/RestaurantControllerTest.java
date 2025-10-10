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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Test
    void shouldChangeDishStateToPublished() throws Exception {
        //Arrange
        Restaurant restaurant = testHelper.saveRestaurant();
        testHelper.saveDish(restaurant.getId(), "Test Dish", DishState.NOT_PUBLISHED ,"This is a test dish", 1.23);
        Dish dish = restaurant.getFullMenu().getFirst();
        //Act & Assert
        mockMvc.perform(
                        patch("/api/restaurants/{restaurantId}/menu/{dishId}/state", restaurant.getId().id(), dish.getId().id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"state\": \"PUBLISHED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("PUBLISHED"))
                .andDo(print());
        //Cleanup
        testHelper.cleanUp();
    }
}