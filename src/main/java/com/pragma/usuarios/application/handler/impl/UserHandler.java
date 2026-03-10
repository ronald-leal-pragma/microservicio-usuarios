package com.pragma.usuarios.application.handler.impl;

import com.pragma.usuarios.application.dto.request.UserRequestDto;
import com.pragma.usuarios.application.handler.IUserHandler;
import com.pragma.usuarios.application.mapper.IUserRequestMapper;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final IUserRequestMapper userRequestMapper;

    @Override
    public void savePropietario(UserRequestDto userRequestDto) {
        log.info("[HANDLER] Iniciando proceso de creación de propietario: correo={}", userRequestDto.getCorreo());
        UserModel userModel = userRequestMapper.toUser(userRequestDto);
        userServicePort.savePropietario(userModel);
        log.info("[HANDLER] Proceso finalizado correctamente para correo: {}", userRequestDto.getCorreo());
    }
}
