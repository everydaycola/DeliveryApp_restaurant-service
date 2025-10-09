package be.kdg.sa.restaurantservice.domain.restaurant.priceCriteria;

public enum PriceCriteria {
    €("Cheap"),
    €€("Normal"),
    €€€("Expensive"),
    €€€€("Premium"),
    UNKNOWN("Unknown");

    private final String Description;

    PriceCriteria(String description) {
        Description = description;
    }
}
