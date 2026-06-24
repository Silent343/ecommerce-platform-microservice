package com.ecommerce.gateway.filter;

import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Filtro global que protege el borde de la plataforma.
 *
 * Estrategia:
 *  - Rutas totalmente públicas (auth, swagger, actuator): pasan sin token.
 *  - GET al catálogo (products, categories): públicos para navegar la tienda.
 *  - Cualquier otra ruta: exige un JWT válido; si falta o es inválido devuelve 401.
 *
 * Cuando el token es válido, propaga la identidad a los microservicios mediante
 * cabeceras X-User-Id y X-User-Role, evitando que cada servicio tenga que
 * re-parsear el token (aunque igual lo validan por defensa en profundidad).
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtValidator jwtValidator;

    // Prefijos que nunca requieren token
    private static final List<String> PUBLIC_PREFIXES = List.of(
            "/api/auth",
            "/swagger-ui",
            "/v3/api-docs",
            "/actuator"
    );

    // Prefijos públicos solo para lecturas (GET)
    private static final List<String> PUBLIC_GET_PREFIXES = List.of(
            "/api/products",
            "/api/categories"
    );

    public JwtAuthenticationFilter(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        HttpMethod method = request.getMethod();

        // Las peticiones preflight de CORS (OPTIONS) no llevan token: dejarlas pasar
        if (HttpMethod.OPTIONS.equals(method)) {
            return chain.filter(exchange);
        }

        if (isPublic(path, method)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "Falta el token de autenticación");
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = jwtValidator.parseAndValidate(token);
            // Propaga identidad a los servicios downstream
            ServerHttpRequest mutated = request.mutate()
                    .header("X-User-Id", claims.getSubject())
                    .header("X-User-Role", String.valueOf(claims.get("role")))
                    .build();
            return chain.filter(exchange.mutate().request(mutated).build());
        } catch (Exception e) {
            return unauthorized(exchange, "Token inválido o expirado");
        }
    }

    private boolean isPublic(String path, HttpMethod method) {
        for (String prefix : PUBLIC_PREFIXES) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }
        if (HttpMethod.GET.equals(method)) {
            for (String prefix : PUBLIC_GET_PREFIXES) {
                if (path.startsWith(prefix)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("X-Auth-Error", message);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        // Se ejecuta temprano, antes del enrutamiento
        return -1;
    }
}
