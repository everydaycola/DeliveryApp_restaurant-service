package be.kdg.sa.restaurantservice.domain.restaurant.priceCriteria;

import be.kdg.sa.restaurantservice.domain.dish.Dish;

import java.util.List;

public interface PriceCriteriaCalculator {
    PriceCriteria Calculate(List<Dish> menu);
}
