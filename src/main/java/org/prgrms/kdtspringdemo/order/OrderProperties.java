package org.prgrms.kdtspringdemo.order;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
public class OrderProperties implements InitializingBean {
    @Value("${kdt.version:v0.0.0}")
    private String version;
    // properties에서 찾아보고, 없으면 : 뒤에있는걸 default로 사용하라는뜻.

    @Value("${kdt.minimum-order-amount}")
    private int minimumOrderAmount;

    @Value("${kdt.support-vendors}")
    private List<String> supportVendors;

    // 그럼 시스템 환경변수를 가져올수있나????
    @Value("${OS}")
    private String OS;


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(MessageFormat.format("[OrderProperties] version -> {0}", version));
        System.out.println(MessageFormat.format("[OrderProperties] minimumOrderAmount -> {0}", minimumOrderAmount));
        System.out.println(MessageFormat.format("[OrderProperties] supportVendors -> {0}", supportVendors));
        System.out.println(MessageFormat.format("[OrderProperties] javaHome -> {0}", OS));
    }
}
