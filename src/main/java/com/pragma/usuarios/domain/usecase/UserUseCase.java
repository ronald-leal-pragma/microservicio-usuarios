package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.exception.DomainException;
import com.pragma.usuarios.domain.exception.ExceptionConstants;
import com.pragma.usuarios.domain.model.RolModel;
import com.pragma.usuarios.domain.model.UserModel;
import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import com.pragma.usuarios.domain.spi.IRolPersistencePort;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {
    private static final int MINIMUM_LEGAL_AGE = 18;
    private static final int DEFAULT_EMPLOYEE_AGE = 25;
    private static final String PROPIETARIO_ROLE_DESC = "Rol de propietario de restaurante";
    private static final String EMPLEADO_ROLE_DESC = "Rol de empleado de restaurante";
    private static final String CLIENTE_ROLE_DESC = "Rol de cliente de plazoleta";

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final IRolPersistencePort rolPersistencePort;

    @Override
    public UserModel savePropietario(UserModel userModel) {
        validateAge(userModel.getFechaNacimiento());
        return processAndSaveUser(
                userModel,
                "propietario",
                ExceptionConstants.ROL_PROPIETARIO,
                PROPIETARIO_ROLE_DESC
        );
    }

    @Override
    public UserModel saveEmployee(UserModel userModel) {
        ensureDefaultBirthDate(userModel);
        return processAndSaveUser(
                userModel,
                "empleado",
                ExceptionConstants.ROL_EMPLEADO,
                EMPLEADO_ROLE_DESC
        );
    }

    @Override
    public UserModel saveClient(UserModel userModel) {
        ensureDefaultBirthDate(userModel);
        return processAndSaveUser(
                userModel,
                "cliente",
                ExceptionConstants.ROL_CLIENTE,
                CLIENTE_ROLE_DESC
        );
    }

    private UserModel processAndSaveUser(UserModel userModel, String userType, String roleName, String roleDesc) {
        log.info("[USE CASE] Iniciando validaciones para crear {}: correo={}", userType, userModel.getCorreo());

        validateUserUniqueness(userModel.getCorreo(), userModel.getDocumentoDeIdentidad());

        log.debug("[USE CASE] Encriptando contraseña");
        userModel.setClave(passwordEncoderPort.encode(userModel.getClave()));

        log.info("[USE CASE] Asignando rol: {}", roleName);
        userModel.setRol(getOrCreateRol(roleName, roleDesc));

        log.info("[USE CASE] Todas las validaciones OK, persistiendo {}", userType);
        return userPersistencePort.saveUser(userModel);
    }

    private void ensureDefaultBirthDate(UserModel userModel) {
        if (userModel.getFechaNacimiento() == null) {
            log.debug("[USE CASE] Fecha de nacimiento no proporcionada, asignando fecha por defecto");
            userModel.setFechaNacimiento(LocalDate.now().minusYears(DEFAULT_EMPLOYEE_AGE));
        }
    }

    private RolModel getOrCreateRol(String rolName, String rolDescription) {
        log.debug("[USE CASE] Buscando rol por nombre: {}", rolName);

        return rolPersistencePort.findByName(rolName)
                .orElseGet(() -> {
                    log.info("[USE CASE] Rol no encontrado, procediendo a crearlo: {}", rolName);
                    Long rolId = getRolIdByName(rolName);
                    RolModel newRol = new RolModel(rolId, rolName, rolDescription);
                    return rolPersistencePort.save(newRol);
                });
    }

    private Long getRolIdByName(String rolName) {
        if (ExceptionConstants.ROL_PROPIETARIO.equals(rolName)) {
            return ExceptionConstants.ROL_PROPIETARIO_ID;
        } else if (ExceptionConstants.ROL_EMPLEADO.equals(rolName)) {
            return ExceptionConstants.ROL_EMPLEADO_ID;
        } else if (ExceptionConstants.ROL_CLIENTE.equals(rolName)) {
            return ExceptionConstants.ROL_CLIENTE_ID;
        }
        throw new DomainException("Rol desconocido: " + rolName);
    }

    private void validateAge(LocalDate fechaNacimiento) {
        log.debug("[USE CASE] Validando edad del propietario");

        Optional.ofNullable(fechaNacimiento)
                .map(fecha -> Period.between(fecha, LocalDate.now()).getYears())
                .filter(age -> age >= MINIMUM_LEGAL_AGE)
                .orElseThrow(() -> {
                    log.warn("[USE CASE] Usuario rechazado: no cumple con la mayoría de edad (requiere {} años)", MINIMUM_LEGAL_AGE);
                    return new DomainException(ExceptionConstants.UNDERAGE_USER_MESSAGE);
                });

        log.debug("[USE CASE] Validación de edad OK");
    }

    private void validateUserUniqueness(String correo, String documento) {
        log.debug("[USE CASE] Verificando duplicidad de usuario: correo={}, documento={}", correo, documento);

        Optional.ofNullable(correo)
                .filter(c -> !userPersistencePort.existsByCorreo(c))
                .orElseThrow(() -> {
                    log.warn("[USE CASE] Correo ya registrado: {}", correo);
                    return new DomainException(ExceptionConstants.USER_EMAIL_ALREADY_EXISTS_MESSAGE);
                });

        Optional.ofNullable(documento)
                .filter(d -> !userPersistencePort.existsByDocumentoDeIdentidad(d))
                .orElseThrow(() -> {
                    log.warn("[USE CASE] Documento ya registrado: {}", documento);
                    return new DomainException(ExceptionConstants.USER_DOCUMENT_ALREADY_EXISTS_MESSAGE);
                });

        log.debug("[USE CASE] Validación de duplicidad superada");
    }
}