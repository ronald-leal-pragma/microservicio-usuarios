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
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userModel.getId());
        claims.put("rol", userModel.getRol().getNombre());

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userModel.getCorreo())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        log.debug("[JWT GENERATOR] Token generado para correo={}, rol={}",
                userModel.getCorreo(), userModel.getRol().getNombre());
        return token;
    }
}
