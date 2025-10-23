package be.kdg.sa.restaurantservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "domain")
public class DomainProperties {
    private int maxDishes;
}
