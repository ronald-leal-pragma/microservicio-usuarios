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
    private static final String PROPIETARIO_ROLE_DESC = "Rol de propietario de restaurante";

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final IRolPersistencePort rolPersistencePort;

    @Override
    public UserModel savePropietario(UserModel userModel) {
        log.info("[USE CASE] Iniciando validaciones para crear propietario: correo={}", userModel.getCorreo());

        validateAge(userModel.getFechaNacimiento());
        validateUserUniqueness(userModel.getCorreo(), userModel.getDocumentoDeIdentidad());

        log.debug("[USE CASE] Encriptando contraseña");
        userModel.setClave(passwordEncoderPort.encode(userModel.getClave()));

        log.info("[USE CASE] Asignando rol: {}", ExceptionConstants.ROL_PROPIETARIO);
        userModel.setRol(getOrCreateRol());

        log.info("[USE CASE] Todas las validaciones OK, persistiendo propietario");
        return userPersistencePort.saveUser(userModel);
    }

    private RolModel getOrCreateRol() {
        log.debug("[USE CASE] Buscando rol por nombre: {}", ExceptionConstants.ROL_PROPIETARIO);

        return rolPersistencePort.findByName(ExceptionConstants.ROL_PROPIETARIO)
                .orElseGet(() -> {
                    log.info("[USE CASE] Rol no encontrado, procediendo a crearlo: {}", ExceptionConstants.ROL_PROPIETARIO);
                    RolModel newRol = new RolModel(
                            null,
                            ExceptionConstants.ROL_PROPIETARIO,
                            UserUseCase.PROPIETARIO_ROLE_DESC
                    );
                    return rolPersistencePort.save(newRol);
                });
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