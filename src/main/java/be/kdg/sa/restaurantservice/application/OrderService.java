package be.kdg.sa.restaurantservice.application;

import be.kdg.sa.restaurantservice.domain.order.OrderRepository;
import org.jmolecules.ddd.annotation.Service;

@Service
public class OrderService {
    private final OrderRepository orders;

    public OrderService(OrderRepository orders) {
        this.orders = orders;
    }
}
