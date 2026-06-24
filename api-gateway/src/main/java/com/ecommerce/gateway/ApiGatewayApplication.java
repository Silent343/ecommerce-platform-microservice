package com.ecommerce.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway: punto de entrada único de la plataforma. Enruta las peticiones
 * a los microservicios descubiertos vía Eureka, aplica CORS y valida tokens
 * JWT en el borde antes de dejar pasar tráfico a rutas protegidas.
 */
@SpringBootApplication
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
