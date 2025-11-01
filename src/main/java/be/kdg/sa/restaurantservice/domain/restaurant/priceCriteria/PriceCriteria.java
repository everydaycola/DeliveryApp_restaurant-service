package be.kdg.sa.restaurantservice.domain.restaurant.priceCriteria;

import lombok.Getter;
import org.jmolecules.ddd.annotation.ValueObject;

@Getter
@ValueObject
public enum PriceCriteria {
    CHEAP("€"),
    NORMAL("€€"),
    EXPENSIVE("€€€"),
    PREMIUM("€€€€"),
    UNKNOWN("Unknown");

    private final String Description;

    PriceCriteria(String description) {
        Description = description;
    }
}
