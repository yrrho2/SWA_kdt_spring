package org.prgrms.kdtspringdemo;

import org.prgrms.kdtspringdemo.order.OrderItem;
import org.prgrms.kdtspringdemo.order.OrderProperties;
import org.prgrms.kdtspringdemo.order.OrderService;
import org.prgrms.kdtspringdemo.voucher.*;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderTester {
    public static void main(String[] args) {
        var applicationContext =  new AnnotationConfigApplicationContext(AppConfiguration.class);
        applicationContext.register(AppConfiguration.class);
        var environment = applicationContext.getEnvironment();
        environment.setActiveProfiles("dev");
        applicationContext.refresh();


//        var version = environment.getProperty("kdt.version");
//        var MinimumOrderAmount = environment.getProperty("kdt.minimum-order-amount", Integer.class);
//        var supportVendos = environment.getProperty("kdt.support-vendors", List.class);
//        var description = environment.getProperty("kdt.description", List.class);
//        System.out.println(MessageFormat.format("{0}", version));
//        System.out.println(MessageFormat.format("{0}", MinimumOrderAmount));
//        System.out.println(MessageFormat.format("{0}", supportVendos));
//        System.out.println(MessageFormat.format("{0}", description));
        var orderProperties = applicationContext.getBean(OrderProperties.class);
        System.out.println(MessageFormat.format("{0}", orderProperties.getVersion()));
        System.out.println(MessageFormat.format("{0}", orderProperties.getMinimumOrderAmount()));
        System.out.println(MessageFormat.format("{0}", orderProperties.getSupportVendors()));
        System.out.println(MessageFormat.format("{0}", orderProperties.getDescription()));

        var customerId = UUID.randomUUID();
        var voucherRepository = applicationContext.getBean(VoucherRepository.class);
        var voucher = voucherRepository.insert(new FixedAmountVoucher(UUID.randomUUID(), 10L));

        System.out.println(MessageFormat.format("is Jdbc Repo -> {0}", voucherRepository instanceof JdbcVoucherRepository));
        System.out.println(MessageFormat.format("is Jdbc Repo -> {0}", voucherRepository.getClass().getCanonicalName()));

        var orderService = applicationContext.getBean(OrderService.class);
        var order = orderService.createOrder(customerId, new ArrayList<OrderItem>(){{
            add(new OrderItem(UUID.randomUUID(), 100L,1));
        }}, voucher.getVoucherId());

        Assert.isTrue(order.totalAmount()==90L, MessageFormat.format("totalAmount {0} is not 90L",order.totalAmount()));
        applicationContext.close();
    }
}
// 실제 자바기반의 application Config를 사용할땐 이걸로 사용함.
// applicationContext에서 Bean을 만들수있음.
// 왜? 강의에서 설명했듯이 application context는 bean 공장을 상속받기때문에.
// AppConfiguration.java 파일에서 @Configuration으로 정의한다고함

// BeanFactoryAnnotationUtils 기능에서, qualified된 Bean을 가져오기위해. applicationContext의 콩공장과, Voucher레포지토리에서, "Memory"라는 Bean을 가져옴,
// 즉 MemoryVoucherRepository를 가져올 수 있게됨.

//var voucherRepository = applicationContext.getBean(VoucherRepository.class); 여기도 뭔지 모름. 전해줄수없음.
// grow