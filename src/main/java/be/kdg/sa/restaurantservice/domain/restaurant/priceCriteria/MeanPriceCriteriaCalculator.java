package be.kdg.sa.restaurantservice.domain.restaurant.priceCriteria;

import be.kdg.sa.restaurantservice.domain.dish.Dish;

import java.util.List;

public class MeanPriceCriteriaCalculator implements PriceCriteriaCalculator {
    @Override
    public PriceCriteria calculate(List<Dish> menu) {
        final var mean = getMean(menu);
        if (mean <= 10) {
            return PriceCriteria.CHEAP;
        } else if (mean <= 30) {
            return PriceCriteria.NORMAL;
        } else if (mean <= 60) {
            return PriceCriteria.EXPENSIVE;
        } else {
            return PriceCriteria.PREMIUM;
        }
    }


    private static double getMean(List<Dish> menu) {
        return menu.stream().map(Dish::getPrice).mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
