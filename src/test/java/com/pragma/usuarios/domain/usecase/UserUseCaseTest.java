package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.domain.exception.DomainException;
import com.pragma.usuarios.domain.exception.ExceptionConstants;
import com.pragma.usuarios.domain.model.RolModel;
import com.pragma.usuarios.domain.model.UserModel;
import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import com.pragma.usuarios.domain.spi.IRolPersistencePort;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;
    @Mock
    private IPasswordEncoderPort passwordEncoderPort;
    @Mock
    private IRolPersistencePort rolPersistencePort;

    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        userUseCase = new UserUseCase(userPersistencePort, passwordEncoderPort, rolPersistencePort);
    }

    @Test
    void savePropietarioShouldSaveWhenDataIsValid() {
        UserModel request = buildUser(LocalDate.now().minusYears(30));
        RolModel propietario = new RolModel(2L, ExceptionConstants.ROL_PROPIETARIO, "Rol propietario");

        when(userPersistencePort.existsByCorreo(request.getCorreo())).thenReturn(false);
        when(userPersistencePort.existsByDocumentoDeIdentidad(request.getDocumentoDeIdentidad())).thenReturn(false);
        when(passwordEncoderPort.encode("1234")).thenReturn("encoded-1234");
        when(rolPersistencePort.findByName(ExceptionConstants.ROL_PROPIETARIO)).thenReturn(Optional.of(propietario));
        when(userPersistencePort.saveUser(any(UserModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserModel result = userUseCase.savePropietario(request);

        assertEquals("encoded-1234", result.getClave());
        assertEquals(ExceptionConstants.ROL_PROPIETARIO, result.getRol().getNombre());
        verify(rolPersistencePort, never()).save(any(RolModel.class));
        verify(userPersistencePort).saveUser(any(UserModel.class));
    }

    @Test
    void savePropietarioShouldThrowWhenUserIsUnderage() {
        UserModel request = buildUser(LocalDate.now().minusYears(17));

        DomainException exception = assertThrows(DomainException.class, () -> userUseCase.savePropietario(request));

        assertEquals(ExceptionConstants.UNDERAGE_USER_MESSAGE, exception.getMessage());
        verifyNoInteractions(passwordEncoderPort, rolPersistencePort);
        verify(userPersistencePort, never()).saveUser(any(UserModel.class));
    }

    @Test
    void savePropietarioShouldThrowWhenEmailAlreadyExists() {
        UserModel request = buildUser(LocalDate.now().minusYears(25));
        when(userPersistencePort.existsByCorreo(request.getCorreo())).thenReturn(true);

        DomainException exception = assertThrows(DomainException.class, () -> userUseCase.savePropietario(request));

        assertEquals(ExceptionConstants.USER_EMAIL_ALREADY_EXISTS_MESSAGE, exception.getMessage());
        verify(userPersistencePort, never()).saveUser(any(UserModel.class));
        verifyNoInteractions(passwordEncoderPort, rolPersistencePort);
    }

    @Test
    void saveClientShouldThrowWhenDocumentAlreadyExists() {
        UserModel request = buildUser(null);
        when(userPersistencePort.existsByCorreo(request.getCorreo())).thenReturn(false);
        when(userPersistencePort.existsByDocumentoDeIdentidad(request.getDocumentoDeIdentidad())).thenReturn(true);

        DomainException exception = assertThrows(DomainException.class, () -> userUseCase.saveClient(request));

        assertEquals(ExceptionConstants.USER_DOCUMENT_ALREADY_EXISTS_MESSAGE, exception.getMessage());
        verify(userPersistencePort, never()).saveUser(any(UserModel.class));
    }

    @Test
    void saveEmployeeShouldAssignDefaultBirthDateWhenMissing() {
        UserModel request = buildUser(null);
        RolModel empleado = new RolModel(4L, ExceptionConstants.ROL_EMPLEADO, "Rol empleado");

        when(userPersistencePort.existsByCorreo(request.getCorreo())).thenReturn(false);
        when(userPersistencePort.existsByDocumentoDeIdentidad(request.getDocumentoDeIdentidad())).thenReturn(false);
        when(passwordEncoderPort.encode("1234")).thenReturn("encoded-1234");
        when(rolPersistencePort.findByName(ExceptionConstants.ROL_EMPLEADO)).thenReturn(Optional.empty());
        when(rolPersistencePort.save(any(RolModel.class))).thenReturn(empleado);
        when(userPersistencePort.saveUser(any(UserModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userUseCase.saveEmployee(request);

        ArgumentCaptor<UserModel> userCaptor = ArgumentCaptor.forClass(UserModel.class);
        verify(userPersistencePort).saveUser(userCaptor.capture());
        assertEquals(LocalDate.now().minusYears(25), userCaptor.getValue().getFechaNacimiento());
        verify(rolPersistencePort).save(any(RolModel.class));
    }

    private UserModel buildUser(LocalDate birthDate) {
        UserModel user = new UserModel();
        user.setNombre("Juan");
        user.setApellido("Perez");
        user.setDocumentoDeIdentidad("123456");
        user.setCelular("3001234567");
        user.setFechaNacimiento(birthDate);
        user.setCorreo("juan@correo.com");
        user.setClave("1234");
        return user;
    }
}

