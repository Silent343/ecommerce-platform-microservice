package com.ecommerce.users.infrastructure.security;

import com.ecommerce.users.application.port.in.TokenProvider;
import com.ecommerce.users.domain.model.aggregate.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * Implementación de TokenProvider basada en JJWT.
 * Emite access tokens (corta duración) y refresh tokens (larga duración),
 * firmados con HMAC-SHA256. El secreto se inyecta desde configuración.
 */
@Component
public class JwtTokenProvider implements TokenProvider {

    private final SecretKey key;
    private final long accessTtlSeconds;
    private final long refreshTtlSeconds;

    public JwtTokenProvider(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.access-ttl-seconds:900}") long accessTtlSeconds,
            @Value("${security.jwt.refresh-ttl-seconds:604800}") long refreshTtlSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTtlSeconds = accessTtlSeconds;
        this.refreshTtlSeconds = refreshTtlSeconds;
    }

    @Override
    public String generateAccessToken(User user) {
        return buildToken(user, accessTtlSeconds, "access");
    }

    @Override
    public String generateRefreshToken(User user) {
        return buildToken(user, refreshTtlSeconds, "refresh");
    }

    private String buildToken(User user, long ttlSeconds, String type) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail().value())
                .claim("role", user.getRole().name())
                .claim("type", type)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ttlSeconds)))
                .signWith(key)
                .compact();
    }

    @Override
    public String extractSubject(String token) {
        return parse(token).getSubject();
    }

    @Override
    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public long accessTokenTtlSeconds() {
        return accessTtlSeconds;
    }
}
