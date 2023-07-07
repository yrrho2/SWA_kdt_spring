package org.prgrms.kdtspringdemo;

import org.prgrms.kdtspringdemo.order.Order;
import org.prgrms.kdtspringdemo.voucher.Voucher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.UUID;
@Configuration
@ComponentScan(basePackages ={"org.prgrms.kdtspringdemo.order", "org.prgrms.kdtspringdemo.voucher", "org.prgrms.kdtspringdemo.configuration"})
//@ComponentScan(basePackages ={"org.prgrms.kdtspringdemo.order", "org.prgrms.kdtspringdemo.voucher"})
// 내가 스캔하고싶은것들만 package에서 찾아씀
// excludeFilters 으로 제외시킬수있음
public class AppConfiguration {
    // Configuration은 bean을 정의하는곳.
    // @Bean은 각 configuration metadata가 되도록 정의함.

}
