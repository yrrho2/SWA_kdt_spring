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
    public VoucherService voucherService(VoucherRepository voucherRepository){
        // 클래스를 작성해주면 메서드의 매개변수로 스프링이 알아서 위에 매개변수에 있는 Bean을 찾아서
        // 객체를 이친구가 만들어질때 전달이 된다,,,?
        // VoucherService라는 Bean을 만들때 Spring이 매개변수에 있는 VoucherRepository Bean을 찾아서 준다는 소리인가?
        // 이게 뭐가다른거지?
        return new VoucherService(voucherRepository);
    }
    @Bean
    public OrderService orderService(VoucherService voucherService ,OrderRepositry orderRepositry){
        return new OrderService(voucherService,orderRepositry);
    }
}
