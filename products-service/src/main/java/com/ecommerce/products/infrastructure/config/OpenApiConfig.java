package com.ecommerce.products.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productsServiceOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Products Service API")
                .description("Microservicio de catálogo e inventario - plataforma e-commerce")
                .version("1.0.0"));
    }
}
