package be.kdg.sa.restaurantservice.domain.restaurant.priceCriteria;

import be.kdg.sa.restaurantservice.domain.dish.Dish;
import be.kdg.sa.restaurantservice.domain.restaurant.Restaurant;

import java.util.List;

public class MeanPriceCriteriaCalculator implements PriceCriteriaCalculator {
    @Override
    public PriceCriteria Calculate(List<Dish> menu) {
        double mean = getMean(menu);
        PriceCriteria priceCriteria = PriceCriteria.UNKNOWN;
        if (mean <= 10) {
            priceCriteria = PriceCriteria.€;
        } else if (mean > 10 && mean <= 30) {
            priceCriteria = PriceCriteria.€€;
        } else if (mean > 30 && mean <= 60) {
            priceCriteria = PriceCriteria.€€€;
        } else if (mean > 60) {
            priceCriteria = PriceCriteria.€€€€;
        }
        return priceCriteria;
    }

    private static double getMean(List<Dish> menu) {
        return menu.stream().map(Dish::getPrice).mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
