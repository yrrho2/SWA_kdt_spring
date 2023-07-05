package org.prgrms.kdtspringdemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class AppConfiguration {
    // Configuration은 bean을 정의하는곳.

    // @Bean은 각 configuration metadata가 되도록 정의함.
    @Bean
    public VoucherRepository voucherRepository(){
        return new VoucherRepository() {
            @Override
            public Optional<Voucher> findById(UUID voucherId) {
                return Optional.empty();
            }
        };
    }
    @Bean
    public OrderRepositry orderRepositry(){
        return new OrderRepositry() {
            @Override
            public void insert(Order order) {

            }
        };
    }
    @Bean
    public VoucherService voucherService(){
        return new VoucherService(voucherRepository());
    }
    @Bean
    public OrderService orderService(){
        return new OrderService(voucherService(),orderRepositry());
    }
}
