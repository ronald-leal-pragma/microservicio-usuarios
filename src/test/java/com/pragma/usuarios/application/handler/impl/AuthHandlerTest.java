package com.pragma.usuarios.application.handler.impl;

import com.pragma.usuarios.application.dto.request.LoginRequestDto;
import com.pragma.usuarios.application.dto.response.LoginResponseDto;
import com.pragma.usuarios.domain.api.IAuthServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthHandlerTest {

    @Mock
    private IAuthServicePort authServicePort;

    private AuthHandler authHandler;

    @BeforeEach
    void setUp() {
        authHandler = new AuthHandler(authServicePort);
    }

    @Test
    void loginShouldReturnTokenResponse() {
        LoginRequestDto request = new LoginRequestDto();
        request.setCorreo("user@correo.com");
        request.setClave("1234");

        when(authServicePort.login("user@correo.com", "1234")).thenReturn("generated-token");

        LoginResponseDto response = authHandler.login(request);

        assertEquals("generated-token", response.getToken());
    }
}

