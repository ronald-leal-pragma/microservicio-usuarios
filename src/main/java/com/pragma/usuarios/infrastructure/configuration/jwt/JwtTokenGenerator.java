package com.pragma.usuarios.infrastructure.configuration.jwt;

import com.pragma.usuarios.domain.model.UserModel;
import com.pragma.usuarios.domain.spi.ITokenGeneratorPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenGenerator implements ITokenGeneratorPort {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(UserModel userModel) {
        log.info("[JWT GENERATOR] Generando token para correo={}, rol={}",
                userModel.getCorreo(), userModel.getRol().getNombre());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userModel.getId());
        claims.put("rol", userModel.getRol().getNombre());

        Date issuedAt = new Date();
        Date expiration = new Date(System.currentTimeMillis() + expirationMs);
        log.debug("[JWT GENERATOR] Claims: id={}, rol={}, expiresAt={}",
                userModel.getId(), userModel.getRol().getNombre(), expiration);

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userModel.getCorreo())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        log.info("[JWT GENERATOR] Token generado exitosamente para correo={}", userModel.getCorreo());
        return token;
    }
}
