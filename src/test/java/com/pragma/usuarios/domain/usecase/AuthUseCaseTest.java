package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.domain.exception.DomainException;
import com.pragma.usuarios.domain.exception.ExceptionConstants;
import com.pragma.usuarios.domain.model.RolModel;
import com.pragma.usuarios.domain.model.UserModel;
import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import com.pragma.usuarios.domain.spi.ITokenGeneratorPort;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;
    @Mock
    private IPasswordEncoderPort passwordEncoderPort;
    @Mock
    private ITokenGeneratorPort tokenGeneratorPort;

    private AuthUseCase authUseCase;

    @BeforeEach
    void setUp() {
        authUseCase = new AuthUseCase(userPersistencePort, passwordEncoderPort, tokenGeneratorPort);
    }

    @Test
    void loginShouldGenerateTokenWhenCredentialsAreValid() {
        UserModel user = new UserModel();
        user.setCorreo("user@correo.com");
        user.setClave("encoded");
        user.setRol(new RolModel(1L, "ADMINISTRADOR", "Admin"));

        when(userPersistencePort.findByCorreo("user@correo.com")).thenReturn(Optional.of(user));
        when(passwordEncoderPort.matches("raw-pass", "encoded")).thenReturn(true);
        when(tokenGeneratorPort.generateToken(user)).thenReturn("jwt-token");

        String token = authUseCase.login("user@correo.com", "raw-pass");

        assertEquals("jwt-token", token);
        verify(tokenGeneratorPort).generateToken(user);
    }

    @Test
    void loginShouldThrowWhenUserDoesNotExist() {
        when(userPersistencePort.findByCorreo("missing@correo.com")).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class,
                () -> authUseCase.login("missing@correo.com", "any"));

        assertEquals(ExceptionConstants.INVALID_CREDENTIALS_MESSAGE, exception.getMessage());
        verify(tokenGeneratorPort, never()).generateToken(any(UserModel.class));
    }

    @Test
    void loginShouldThrowWhenPasswordDoesNotMatch() {
        UserModel user = new UserModel();
        user.setCorreo("user@correo.com");
        user.setClave("encoded");
        user.setRol(new RolModel(3L, "CLIENTE", "Cliente"));

        when(userPersistencePort.findByCorreo("user@correo.com")).thenReturn(Optional.of(user));
        when(passwordEncoderPort.matches("raw-pass", "encoded")).thenReturn(false);

        DomainException exception = assertThrows(DomainException.class,
                () -> authUseCase.login("user@correo.com", "raw-pass"));

        assertEquals(ExceptionConstants.INVALID_CREDENTIALS_MESSAGE, exception.getMessage());
        verify(tokenGeneratorPort, never()).generateToken(any(UserModel.class));
    }
}

