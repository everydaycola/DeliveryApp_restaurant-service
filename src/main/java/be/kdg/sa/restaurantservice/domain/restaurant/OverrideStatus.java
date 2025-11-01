package be.kdg.sa.restaurantservice.domain.restaurant;

import lombok.Getter;
import org.jmolecules.ddd.annotation.ValueObject;

@Getter
@ValueObject
public enum OverrideStatus {
    NONE(null),
    FORCED_OPEN(true),
    FORCED_CLOSED(false);

    private final Boolean isOpen;

    OverrideStatus(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    static OverrideStatus fromBoolean(Boolean isOpen) {
        return isOpen ? FORCED_OPEN : FORCED_CLOSED;
    }
}
