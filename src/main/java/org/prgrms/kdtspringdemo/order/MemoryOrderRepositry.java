package org.prgrms.kdtspringdemo.order;

import org.prgrms.kdtspringdemo.order.Order;
import org.prgrms.kdtspringdemo.order.OrderRepositry;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemoryOrderRepositry implements OrderRepositry {
    private final Map<UUID, Order> storage = new ConcurrentHashMap<>();
    @Override
    public Order insert(Order order) {
        storage.put(order.getOrderId()  ,order);
        return order;

    }
}
