package com.ecommerce.orders.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ordersServiceOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Orders Service API")
                .description("Microservicio de carrito y pedidos - plataforma e-commerce")
                .version("1.0.0"));
    }
}
