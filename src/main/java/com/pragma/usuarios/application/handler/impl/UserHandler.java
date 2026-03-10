package com.pragma.usuarios.application.handler.impl;

import com.pragma.usuarios.application.dto.request.UserRequestDto;
import com.pragma.usuarios.application.dto.response.UserResponseDto;
import com.pragma.usuarios.application.handler.IUserHandler;
import com.pragma.usuarios.application.mapper.IUserRequestMapper;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.model.UserModel;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import com.pragma.usuarios.infrastructure.exception.NoDataFoundException;
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
    private final IUserPersistencePort userPersistencePort;

    @Override
    public void savePropietario(UserRequestDto userRequestDto) {
        log.info("[HANDLER] Iniciando proceso de creación de propietario: correo={}", userRequestDto.getCorreo());
        UserModel userModel = userRequestMapper.toUser(userRequestDto);
        userServicePort.savePropietario(userModel);
        log.info("[HANDLER] Proceso finalizado correctamente para correo: {}", userRequestDto.getCorreo());
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        log.info("[HANDLER] Buscando usuario por id={}", id);
        UserModel userModel = userPersistencePort.findById(id)
                .orElseThrow(() -> {
                    log.warn("[HANDLER] Usuario no encontrado: id={}", id);
                    return new NoDataFoundException();
                });
        log.info("[HANDLER] Usuario encontrado: id={}, correo={}, rol={}",
                userModel.getId(), userModel.getCorreo(),
                userModel.getRol() != null ? userModel.getRol().getNombre() : "sin rol");
        UserResponseDto dto = new UserResponseDto();
        dto.setId(userModel.getId());
        dto.setNombre(userModel.getNombre());
        dto.setApellido(userModel.getApellido());
        dto.setCorreo(userModel.getCorreo());
        dto.setRol(userModel.getRol() != null ? userModel.getRol().getNombre() : null);
        return dto;
    }
}
