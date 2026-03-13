package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.domain.api.IAuthServicePort;
import com.pragma.usuarios.domain.exception.DomainException;
import com.pragma.usuarios.domain.exception.ExceptionConstants;
import com.pragma.usuarios.domain.model.UserModel;
import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import com.pragma.usuarios.domain.spi.ITokenGeneratorPort;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AuthUseCase implements IAuthServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final ITokenGeneratorPort tokenGeneratorPort;

    @Override
    public String login(String correo, String clave) {
        log.info("[USE CASE] Intento de login para correo={}", correo);

        UserModel authenticatedUser = userPersistencePort.findByCorreo(correo)
                .filter(user -> passwordEncoderPort.matches(clave, user.getClave()))
                .orElseThrow(() -> {
                    log.warn("[USE CASE] Credenciales inválidas para correo={}", correo);
                    return new DomainException(ExceptionConstants.INVALID_CREDENTIALS_MESSAGE);
                });

        log.info("[USE CASE] Login exitoso para correo={}, rol={}", correo, authenticatedUser.getRol().getNombre());
        return tokenGeneratorPort.generateToken(authenticatedUser);
    }
}