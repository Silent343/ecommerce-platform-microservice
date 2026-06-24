package com.ecommerce.orders.infrastructure.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Provee un RestClient.Builder con @LoadBalanced, lo que habilita el uso de
 * URLs lb:// (o nombres de servicio) que Eureka resuelve y el LoadBalancer
 * reparte entre instancias. Sin esto, "http://PRODUCTS-SERVICE" no se podría
 * resolver.
 */
@Configuration
public class RestClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }
}
