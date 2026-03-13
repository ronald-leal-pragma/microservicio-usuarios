package com.pragma.usuarios.infrastructure.input.rest;

import com.pragma.usuarios.application.dto.request.ClientRequestDto;
import com.pragma.usuarios.application.dto.request.EmployeeRequestDto;
import com.pragma.usuarios.application.dto.request.UserRequestDto;
import com.pragma.usuarios.application.dto.response.UserCreatedResponseDto;
import com.pragma.usuarios.application.dto.response.UserResponseDto;
import com.pragma.usuarios.application.handler.IUserHandler;
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
class UserRestControllerTest {

    @Mock
    private IUserHandler userHandler;

    private UserRestController controller;

    @BeforeEach
    void setUp() {
        controller = new UserRestController(userHandler);
    }

    @Test
    void savePropietarioShouldReturnCreatedResponse() {
        UserRequestDto request = new UserRequestDto();
        request.setCorreo("owner@correo.com");

        UserCreatedResponseDto created = UserCreatedResponseDto.builder().id(1L).correo("owner@correo.com").build();
        when(userHandler.savePropietario(request)).thenReturn(created);

        ResponseEntity<UserCreatedResponseDto> response = controller.savePropietario(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void saveEmployeeShouldReturnCreatedResponse() {
        EmployeeRequestDto request = new EmployeeRequestDto();
        request.setCorreo("employee@correo.com");

        UserCreatedResponseDto created = UserCreatedResponseDto.builder().id(2L).correo("employee@correo.com").build();
        when(userHandler.saveEmployee(request)).thenReturn(created);

        ResponseEntity<UserCreatedResponseDto> response = controller.saveEmployee(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2L, response.getBody().getId());
    }

    @Test
    void saveClientShouldReturnCreatedResponse() {
        ClientRequestDto request = new ClientRequestDto();
        request.setCorreo("client@correo.com");

        UserCreatedResponseDto created = UserCreatedResponseDto.builder().id(3L).correo("client@correo.com").build();
        when(userHandler.saveClient(request)).thenReturn(created);

        ResponseEntity<UserCreatedResponseDto> response = controller.saveClient(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3L, response.getBody().getId());
    }

    @Test
    void getUserByIdShouldReturnOkResponse() {
        UserResponseDto userResponseDto = UserResponseDto.builder().id(20L).correo("user@correo.com").build();
        when(userHandler.getUserById(20L)).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = controller.getUserById(20L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(20L, response.getBody().getId());
    }
}

