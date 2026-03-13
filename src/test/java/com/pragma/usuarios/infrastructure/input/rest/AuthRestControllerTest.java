package com.pragma.usuarios.infrastructure.input.rest;

import com.pragma.usuarios.application.dto.request.LoginRequestDto;
import com.pragma.usuarios.application.dto.response.LoginResponseDto;
import com.pragma.usuarios.application.handler.IAuthHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthRestControllerTest {

    @Mock
    private IAuthHandler authHandler;

    private AuthRestController controller;

    @BeforeEach
    void setUp() {
        controller = new AuthRestController(authHandler);
    }

    @Test
    void loginShouldReturnOkResponse() {
        LoginRequestDto request = new LoginRequestDto();
        request.setCorreo("user@correo.com");
        request.setClave("1234");

        when(authHandler.login(request)).thenReturn(new LoginResponseDto("jwt-token"));

        ResponseEntity<LoginResponseDto> response = controller.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwt-token", response.getBody().getToken());
    }
}

