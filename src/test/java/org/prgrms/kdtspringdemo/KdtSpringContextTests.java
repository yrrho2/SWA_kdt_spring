package org.prgrms.kdtspringdemo;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.prgrms.kdtspringdemo.order.MemoryOrderRepositry;
import org.prgrms.kdtspringdemo.order.OrderItem;
import org.prgrms.kdtspringdemo.order.OrderService;
import org.prgrms.kdtspringdemo.order.OrderStatus;
import org.prgrms.kdtspringdemo.voucher.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

// @ExtendWith(SpringExtension.class) // framework를 사용해서 실제로 만듬.
// @ContextConfiguration() // 이렇게 만들어야한다.

@SpringJUnitConfig
@ActiveProfiles("test") // test프로파일인 메모리어쩌구가 사용되는듯
public class KdtSpringContextTests {
    @Autowired
    ApplicationContext context;

    @Configuration
    @ComponentScan(
            basePackages = {"org.prgrms.kdtspringdemo.voucher","org.prgrms.kdtspringdemo.order",}
    )
    static class Config{
    }
    @Autowired
    OrderService orderService;
    @Autowired
    VoucherRepository voucherRepository;

    @Test
    @DisplayName("ApplicationContext가 생성되야 한다")
    public void testApplicationContext(){
        assertThat(context, notNullValue());
    }

    @Test
    @DisplayName("VoucherRepository가 빈으로 등록되어 있어야 한다.")
    public void testVoucherRepositoryCreation(){
        var bean = context.getBean(VoucherRepository.class);
        assertThat(bean, notNullValue());
    }
    @Test
    @DisplayName("orderService를 사용해서 주문을 생성할 수 있다.")
    public void testOrderService(){
//      Given
        var fixedAmountVoucher = new FixedAmountVoucher(UUID.randomUUID(), 100);
        voucherRepository.insert(fixedAmountVoucher);

//      When
        var order = orderService.createOrder(UUID.randomUUID(),
                List.of(new OrderItem(UUID.randomUUID(), 200, 1)),
                fixedAmountVoucher.getVoucherId());

//      Then
        assertThat(order.totalAmount(), is(100L));
        assertThat(order.getVoucher().isEmpty(), is(false));
        assertThat(order.getVoucher().get().getVoucherId(), is(fixedAmountVoucher.getVoucherId()));
        assertThat(order.getOrderStatus(), is(OrderStatus.ACCEPTED));

    }
}
