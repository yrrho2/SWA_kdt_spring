package org.prgrms.kdtspringdemo;

import java.util.Optional;
import java.util.UUID;

public class OrderContext {
    //개별 클래스에 대한 책임을 가지고있음
    public VoucherRepository voucherRepository(){
        return new VoucherRepository() {
            @Override
            public Optional<Voucher> findById(UUID voucherId) {
                return Optional.empty();
            }
        };
    }
    public OrderRepositry orderRepositry(){
        return new OrderRepositry() {
            @Override
            public void insert(Order order) {

            }
        };
    }
    public VoucherService voucherService(){
        return new VoucherService(voucherRepository());
    }
    public OrderService orderService(){
        return new OrderService(voucherService(),orderRepositry());
    }
    // OrderService가 어떤 voucerService와 어떤 orderRepository를 쓰는지
    // 관계에 대한 책임도 가지고있다.
}
