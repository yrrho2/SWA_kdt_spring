package org.prgrms.kdtspringdemo.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;


// Yaml을보면 데이터들이 Class처럼 개별 필드같이 그룹핑되어있으니
// 이것을 그룹핑시켜주는 타입을 만들어주는게 ConfigurationProperties
// 큰 프로젝트에선 이렇게 타입을 주입시켜서 필드에 적용하는게 좋다
// api, DB, server따로 그룹화시켜서 필요할때마다 주입받아 사용가능
// 규모가 작다면 Valueanotation으로도 충분함.
@Configuration
@ConfigurationProperties(prefix="kdt")
public class OrderProperties implements InitializingBean {

    private final static Logger logger = LoggerFactory.getLogger(OrderProperties.class);

    private String version;
    // properties에서 찾아보고, 없으면 : 뒤에있는걸 default로 사용하라는뜻.
    private int minimumOrderAmount;
    private List<String> supportVendors;
    private String description;

    // 그럼 시스템 환경변수를 가져올수있나????
    @Value("${OS}")
    private String OS;


    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("[OrderProperties] version -> {}", version);
        logger.debug("[OrderProperties] minimumOrderAmount -> {}", minimumOrderAmount);
        logger.debug("[OrderProperties] supportVendors -> {}", supportVendors);
        logger.debug("[OrderProperties] javaHome -> {}", OS);
    }

    public String getVersion() {
        return version;
    }

    public int getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public List<String> getSupportVendors() {
        return supportVendors;
    }

    public String getDescription() {
        return description;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setMinimumOrderAmount(int minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }

    public void setSupportVendors(List<String> supportVendors) {
        this.supportVendors = supportVendors;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
