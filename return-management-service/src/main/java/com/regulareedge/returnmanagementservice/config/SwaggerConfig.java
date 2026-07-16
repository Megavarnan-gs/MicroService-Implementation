package com.regulareedge.returnmanagementservice.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
public class SwaggerConfig {

    @Bean
    public OpenAPI returnManagementServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Return Management Service API")
                        .version("1.0.0")
                        .description("Return Management Service for RegulareEdge"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
