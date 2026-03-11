package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.domain.api.IAuthServicePort;
import com.pragma.usuarios.domain.exception.DomainException;
import com.pragma.usuarios.domain.exception.ExceptionConstants;
import com.pragma.usuarios.domain.model.UserModel;
import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import com.pragma.usuarios.domain.spi.ITokenGeneratorPort;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class AuthUseCase implements IAuthServicePort {

    private static final Logger log = LoggerFactory.getLogger(AuthUseCase.class);

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final ITokenGeneratorPort tokenGeneratorPort;

    @Override
    public String login(String correo, String clave) {
        log.info("[USE CASE] Intento de login para correo={}", correo);

        UserModel user = userPersistencePort.findByCorreo(correo)
                .orElseThrow(() -> {
                    log.warn("[USE CASE] Usuario no encontrado: {}", correo);
                    return new DomainException(ExceptionConstants.INVALID_CREDENTIALS_MESSAGE);
                });

        if (!passwordEncoderPort.matches(clave, user.getClave())) {
            log.warn("[USE CASE] Contraseña incorrecta para correo={}", correo);
            throw new DomainException(ExceptionConstants.INVALID_CREDENTIALS_MESSAGE);
        }

        log.info("[USE CASE] Login exitoso para correo={}, rol={}", correo, user.getRol().getNombre());
        return tokenGeneratorPort.generateToken(user);
    }
}
