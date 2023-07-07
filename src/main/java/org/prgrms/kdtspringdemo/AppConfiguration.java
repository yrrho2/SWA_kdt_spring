package org.prgrms.kdtspringdemo;

import org.prgrms.kdtspringdemo.order.Order;
import org.prgrms.kdtspringdemo.voucher.Voucher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.UUID;
@Configuration
@ComponentScan(basePackages ={"org.prgrms.kdtspringdemo.order", "org.prgrms.kdtspringdemo.voucher", "org.prgrms.kdtspringdemo.configuration"})

public class AppConfiguration {
    @Bean(initMethod = "init")
    public BeanOne beanOne() {
        return new BeanOne();

    }
}
class BeanOne implements InitializingBean {
    public void init(){
        System.out.println("[BeanOne] init called!");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("[BeanOne] afterPropertiesSet called!");
    }
}