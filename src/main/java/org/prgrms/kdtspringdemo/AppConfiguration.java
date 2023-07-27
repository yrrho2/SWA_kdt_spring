package org.prgrms.kdtspringdemo;

import org.prgrms.kdtspringdemo.configuration.YamlPropertiesFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.AliasFor;

@Configuration
@ComponentScan(basePackages ={"org.prgrms.kdtspringdemo.order",
        "org.prgrms.kdtspringdemo.voucher",
        "org.prgrms.kdtspringdemo.configuration"
        })
// spring boot는 yaml을 지원. spring frame work는 지원안함
@PropertySource(value = "application.yaml", factory = YamlPropertiesFactory.class)
@EnableConfigurationProperties // spring boot꺼니까 framework에게 알려줌. 쓰라고
public class AppConfiguration {
}
