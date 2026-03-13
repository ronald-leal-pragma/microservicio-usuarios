package com.pragma.usuarios.infrastructure.configuration.jwt;

import com.pragma.usuarios.domain.model.RolModel;
import com.pragma.usuarios.domain.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenGeneratorTest {

    private static final String SECRET = "my-super-secret-key-with-32-bytes-minimum!";

    private JwtTokenGenerator jwtTokenGenerator;

    @BeforeEach
    void setUp() {
        jwtTokenGenerator = new JwtTokenGenerator();
        ReflectionTestUtils.setField(jwtTokenGenerator, "secret", SECRET);
        ReflectionTestUtils.setField(jwtTokenGenerator, "expirationMs", 60_000L);
    }

    @Test
    void generateTokenShouldContainExpectedClaims() {
        UserModel user = new UserModel();
        user.setId(10L);
        user.setCorreo("jwt@correo.com");
        user.setRol(new RolModel(2L, "PROPIETARIO", "Rol de propietario"));

        String token = jwtTokenGenerator.generateToken(user);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("jwt@correo.com", claims.getSubject());
        assertEquals(10, claims.get("id"));
        assertEquals("PROPIETARIO", claims.get("rol"));
        assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
    }
}

