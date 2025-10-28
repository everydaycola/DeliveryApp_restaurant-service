package be.kdg.sa.restaurantservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "domain")
public class DomainProperties {
    private final int maxDishes;
    private final int orderTimeout;
}
