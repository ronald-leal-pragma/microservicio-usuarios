package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.exception.DomainException;
import com.pragma.usuarios.domain.exception.ExceptionConstants;
import com.pragma.usuarios.domain.model.RolModel;
import com.pragma.usuarios.domain.model.UserModel;
import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;

public class UserUseCase implements IUserServicePort {

    private static final Logger log = LoggerFactory.getLogger(UserUseCase.class);

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;

    public UserUseCase(IUserPersistencePort userPersistencePort,
                       IPasswordEncoderPort passwordEncoderPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public UserModel savePropietario(UserModel userModel) {
        log.info("[USE CASE] Validando edad del propietario: fechaNacimiento={}", userModel.getFechaNacimiento());
        validateAge(userModel.getFechaNacimiento());
        log.info("[USE CASE] Validación de edad OK");

        log.debug("[USE CASE] Encriptando contraseña");
        userModel.setClave(passwordEncoderPort.encode(userModel.getClave()));

        log.info("[USE CASE] Asignando rol PROPIETARIO");
        userModel.setRol(new RolModel(
                ExceptionConstants.ROL_PROPIETARIO_ID,
                ExceptionConstants.ROL_PROPIETARIO,
                "Rol de propietario de restaurante"
        ));

        log.info("[USE CASE] Persistiendo propietario: correo={}", userModel.getCorreo());
        return userPersistencePort.saveUser(userModel);
}

    private void validateAge(LocalDate fechaNacimiento) {
        int age = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        log.debug("[USE CASE] Edad calculada: {} años", age);
        if (age < 18) {
            log.warn("[USE CASE] Usuario menor de edad: {} años", age);
            throw new DomainException(ExceptionConstants.UNDERAGE_USER_MESSAGE);
        }
    }
}
