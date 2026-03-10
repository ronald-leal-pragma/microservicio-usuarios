package com.pragma.usuarios.application.handler.impl;

import com.pragma.usuarios.application.dto.request.LoginRequestDto;
import com.pragma.usuarios.application.dto.response.LoginResponseDto;
import com.pragma.usuarios.application.handler.IAuthHandler;
import com.pragma.usuarios.domain.api.IAuthServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthHandler implements IAuthHandler {

    private final IAuthServicePort authServicePort;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        log.info("[HANDLER] Procesando solicitud de login para correo={}", loginRequestDto.getCorreo());
        String token = authServicePort.login(loginRequestDto.getCorreo(), loginRequestDto.getClave());
        log.info("[HANDLER] Login exitoso para correo={}", loginRequestDto.getCorreo());
        return new LoginResponseDto(token);
    }
}
