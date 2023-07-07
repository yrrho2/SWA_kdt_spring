package org.prgrms.kdtspringdemo;

import org.prgrms.kdtspringdemo.order.Order;
import org.prgrms.kdtspringdemo.voucher.Voucher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Optional;
import java.util.UUID;
@Configuration
@ComponentScan(basePackages ={"org.prgrms.kdtspringdemo.order",
        "org.prgrms.kdtspringdemo.voucher",
        "org.prgrms.kdtspringdemo.configuration"
        })
@PropertySource("application.properties")
public class AppConfiguration {
}
