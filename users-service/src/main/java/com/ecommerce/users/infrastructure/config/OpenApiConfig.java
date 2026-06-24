package com.ecommerce.users.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI usersServiceOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Users Service API")
                .description("Microservicio de identidad y perfiles - plataforma e-commerce")
                .version("1.0.0"));
    }
}
