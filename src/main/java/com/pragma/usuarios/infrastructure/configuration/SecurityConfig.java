package com.pragma.usuarios.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            // Login y Swagger son públicos
            .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            // Crear propietario requiere token de ADMINISTRADOR (enviado desde plazoleta o directamente)
            .antMatchers(HttpMethod.POST, "/user/").permitAll()
            .anyRequest().authenticated();

        return http.build();
    }
}
