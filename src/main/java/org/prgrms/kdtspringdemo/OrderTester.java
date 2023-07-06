package org.prgrms.kdtspringdemo;

import org.prgrms.kdtspringdemo.order.OrderItem;
import org.prgrms.kdtspringdemo.order.OrderService;
import org.prgrms.kdtspringdemo.voucher.FixedAmountVoucher;
import org.prgrms.kdtspringdemo.voucher.VoucherRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;

public class OrderTester {
    public static void main(String[] args) {
        var applicationContext =  new AnnotationConfigApplicationContext(AppConfiguration.class);


        var customerId = UUID.randomUUID();
        var voucherRepository = applicationContext.getBean(VoucherRepository.class);
        var voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));
        var orderService = applicationContext.getBean(OrderService.class);
        var order = orderService.createOrder(customerId, new ArrayList<OrderItem>(){{
            add(new OrderItem(UUID.randomUUID(), 100L,1));
        }}, voucher.getVoucherId());

        Assert.isTrue(order.totalAmount()==90L, MessageFormat.format("totalAmount {0} is not 90L",order.totalAmount()));
    }
}
// 실제 자바기반의 application Config를 사용할땐 이걸로 사용함.
// applicationContext에서 Bean을 만들수있음.
// 왜? 강의에서 설명했듯이 application context는 bean 공장을 상속받기때문에.
// AppConfiguration.java 파일에서 @Configuration으로 정의한다고함