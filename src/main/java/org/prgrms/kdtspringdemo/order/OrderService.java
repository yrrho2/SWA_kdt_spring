package org.prgrms.kdtspringdemo.order;

import org.prgrms.kdtspringdemo.configuration.VersionProvider;
import org.prgrms.kdtspringdemo.voucher.VoucherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final VoucherService voucherService;
    private final OrderRepositry orderRepositry;

    public OrderService(VoucherService voucherService, OrderRepositry orderRepositry) {
        this.voucherService = voucherService;
        this.orderRepositry = orderRepositry;
    }
    public Order createOrder(UUID customerId, List<OrderItem> orderItems){
        var order = new Order(UUID.randomUUID(), customerId, orderItems);
        return orderRepositry.insert(order);

    }
    public Order createOrder(UUID customerId, List<OrderItem> orderItems, UUID voucherID){
        var voucher = voucherService.getVoucher(voucherID);
        var order = new Order(UUID.randomUUID(), customerId, orderItems, voucher);
        orderRepositry.insert(order);
        voucherService.useVoucher(voucher);
        return order;
    }
}
