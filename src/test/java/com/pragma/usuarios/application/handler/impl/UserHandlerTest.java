package com.pragma.usuarios.application.handler.impl;

import com.pragma.usuarios.application.dto.request.ClientRequestDto;
import com.pragma.usuarios.application.dto.request.EmployeeRequestDto;
import com.pragma.usuarios.application.dto.request.UserRequestDto;
import com.pragma.usuarios.application.dto.response.UserCreatedResponseDto;
import com.pragma.usuarios.application.dto.response.UserResponseDto;
import com.pragma.usuarios.application.mapper.IUserRequestMapper;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.model.RolModel;
import com.pragma.usuarios.domain.model.UserModel;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import com.pragma.usuarios.infrastructure.exception.NoDataFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private IUserServicePort userServicePort;
    @Mock
    private IUserRequestMapper userRequestMapper;
    @Mock
    private IUserPersistencePort userPersistencePort;

    private UserHandler userHandler;

    @BeforeEach
    void setUp() {
        userHandler = new UserHandler(userServicePort, userRequestMapper, userPersistencePort);
    }

    @Test
    void savePropietarioShouldMapAndReturnCreatedDto() {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setCorreo("owner@correo.com");

        UserModel requestModel = new UserModel();
        UserModel savedModel = buildSavedUser(7L, "owner@correo.com", "PROPIETARIO");

        when(userRequestMapper.toUser(requestDto)).thenReturn(requestModel);
        when(userServicePort.savePropietario(requestModel)).thenReturn(savedModel);

        UserCreatedResponseDto response = userHandler.savePropietario(requestDto);

        assertEquals(7L, response.getId());
        assertEquals("owner@correo.com", response.getCorreo());
        assertEquals("PROPIETARIO", response.getRol());
        assertEquals(savedModel.getCreadoEn().toString(), response.getCreadoEn());
    }

    @Test
    void saveEmployeeShouldMapAndReturnCreatedDto() {
        EmployeeRequestDto requestDto = new EmployeeRequestDto();
        requestDto.setCorreo("employee@correo.com");

        UserModel requestModel = new UserModel();
        UserModel savedModel = buildSavedUser(8L, "employee@correo.com", "EMPLEADO");

        when(userRequestMapper.toUserFromEmployee(requestDto)).thenReturn(requestModel);
        when(userServicePort.saveEmployee(requestModel)).thenReturn(savedModel);

        UserCreatedResponseDto response = userHandler.saveEmployee(requestDto);

        assertEquals(8L, response.getId());
        assertEquals("EMPLEADO", response.getRol());
    }

    @Test
    void saveClientShouldMapAndReturnCreatedDto() {
        ClientRequestDto requestDto = new ClientRequestDto();
        requestDto.setCorreo("client@correo.com");

        UserModel requestModel = new UserModel();
        UserModel savedModel = buildSavedUser(9L, "client@correo.com", "CLIENTE");

        when(userRequestMapper.toUserFromClient(requestDto)).thenReturn(requestModel);
        when(userServicePort.saveClient(requestModel)).thenReturn(savedModel);

        UserCreatedResponseDto response = userHandler.saveClient(requestDto);

        assertEquals(9L, response.getId());
        assertEquals("CLIENTE", response.getRol());
    }

    @Test
    void getUserByIdShouldReturnMappedResponse() {
        UserModel user = buildSavedUser(5L, "find@correo.com", "ADMINISTRADOR");
        when(userPersistencePort.findById(5L)).thenReturn(Optional.of(user));

        UserResponseDto response = userHandler.getUserById(5L);

        assertEquals(5L, response.getId());
        assertEquals("find@correo.com", response.getCorreo());
        assertEquals("ADMINISTRADOR", response.getRol());
    }

    @Test
    void getUserByIdShouldThrowWhenUserIsMissing() {
        when(userPersistencePort.findById(77L)).thenReturn(Optional.empty());

        NoDataFoundException exception = assertThrows(NoDataFoundException.class,
                () -> userHandler.getUserById(77L));

        assertTrue(exception.getMessage().contains("77"));
    }

    private UserModel buildSavedUser(Long id, String correo, String rolNombre) {
        UserModel user = new UserModel();
        user.setId(id);
        user.setNombre("Nombre");
        user.setApellido("Apellido");
        user.setCorreo(correo);
        user.setDocumentoDeIdentidad("123456");
        user.setCelular("3001234567");
        user.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        user.setRol(new RolModel(1L, rolNombre, "desc"));
        user.setCreadoEn(Instant.parse("2025-01-01T00:00:00Z"));
        return user;
    }
}

