package com.kunal.clientinfo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "com.kunal.clientinfo")
@Getter
public class ApplicationConfig {
    @Value("${dataCenter}")
    private String dataCenter;
}
