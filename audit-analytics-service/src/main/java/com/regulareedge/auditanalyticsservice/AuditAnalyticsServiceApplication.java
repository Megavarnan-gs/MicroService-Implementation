package com.regulareedge.auditanalyticsservice;

import com.regulareedge.auditanalyticsservice.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(JwtProperties.class)
public class AuditAnalyticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuditAnalyticsServiceApplication.class, args);
    }
}
