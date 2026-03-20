package com.pragma.usuarios.application.handler.impl;

import com.pragma.usuarios.application.dto.request.ClientRequestDto;
import com.pragma.usuarios.application.dto.request.EmployeeRequestDto;
import com.pragma.usuarios.application.dto.request.UserRequestDto;
import com.pragma.usuarios.application.dto.response.UserCreatedResponseDto;
import com.pragma.usuarios.application.dto.response.UserResponseDto;
import com.pragma.usuarios.application.handler.IUserHandler;
import com.pragma.usuarios.application.mapper.IUserRequestMapper;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.model.RolModel;
import com.pragma.usuarios.domain.model.UserModel;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import com.pragma.usuarios.infrastructure.exception.NoDataFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final IUserRequestMapper userRequestMapper;
    private final IUserPersistencePort userPersistencePort;

    @Override
    public UserCreatedResponseDto savePropietario(UserRequestDto userRequestDto) {
        log.info("[HANDLER] Iniciando proceso de creación de propietario: correo={}", userRequestDto.getCorreo());

        UserModel userModel = userRequestMapper.toUser(userRequestDto);
        UserModel saved = userServicePort.savePropietario(userModel);

        log.info("[HANDLER] Proceso finalizado correctamente para correo: {}", userRequestDto.getCorreo());

        return UserCreatedResponseDto.builder()
                .id(saved.getId())
                .nombre(saved.getNombre())
                .apellido(saved.getApellido())
                .correo(saved.getCorreo())
                .rol(Optional.ofNullable(saved.getRol()).map(RolModel::getNombre).orElse(null))
                .creadoEn(Optional.ofNullable(saved.getCreadoEn()).map(Object::toString).orElse(null))
                .build();
    }

    @Override
    public UserCreatedResponseDto saveEmployee(EmployeeRequestDto employeeRequestDto) {
        log.info("[HANDLER] Iniciando proceso de creación de empleado: correo={}", employeeRequestDto.getCorreo());

        UserModel userModel = userRequestMapper.toUserFromEmployee(employeeRequestDto);
        UserModel saved = userServicePort.saveEmployee(userModel);

        log.info("[HANDLER] Proceso finalizado correctamente para empleado: {}", employeeRequestDto.getCorreo());

        return UserCreatedResponseDto.builder()
                .id(saved.getId())
                .nombre(saved.getNombre())
                .apellido(saved.getApellido())
                .correo(saved.getCorreo())
                .rol(Optional.ofNullable(saved.getRol()).map(RolModel::getNombre).orElse(null))
                .creadoEn(Optional.ofNullable(saved.getCreadoEn()).map(Object::toString).orElse(null))
                .build();
    }

    @Override
    public UserCreatedResponseDto saveClient(ClientRequestDto clientRequestDto) {
        log.info("[HANDLER] Iniciando proceso de creación de cliente: correo={}", clientRequestDto.getCorreo());

        UserModel userModel = userRequestMapper.toUserFromClient(clientRequestDto);
        UserModel saved = userServicePort.saveClient(userModel);

        log.info("[HANDLER] Proceso finalizado correctamente para cliente: {}", clientRequestDto.getCorreo());

        return UserCreatedResponseDto.builder()
                .id(saved.getId())
                .nombre(saved.getNombre())
                .apellido(saved.getApellido())
                .correo(saved.getCorreo())
                .rol(Optional.ofNullable(saved.getRol()).map(RolModel::getNombre).orElse(null))
                .creadoEn(Optional.ofNullable(saved.getCreadoEn()).map(Object::toString).orElse(null))
                .build();
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        log.info("[HANDLER] Buscando usuario por id={}", id);

        UserModel userModel = userPersistencePort.findById(id)
                .orElseThrow(() -> {
                    log.warn("[HANDLER] Usuario no encontrado: id={}", id);
                    return new NoDataFoundException("No se encontró el recurso solicitado (usuario con ID " + id + ")");
                });

        log.info("[HANDLER] Usuario encontrado: id={}, correo={}, rol={}",
                userModel.getId(), userModel.getCorreo(),
                Optional.ofNullable(userModel.getRol()).map(r -> r.getNombre()).orElse("sin rol"));

        return UserResponseDto.builder()
                .id(userModel.getId())
                .nombre(userModel.getNombre())
                .correo(userModel.getCorreo())
                .rol(Optional.ofNullable(userModel.getRol()).map(RolModel::getNombre).orElse(null))
                .fechaCreacion(Optional.ofNullable(userModel.getCreadoEn()).map(Object::toString).orElse(null))
                .build();
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        log.info("[HANDLER] Buscando usuario por email={}", email);

        UserModel userModel = userPersistencePort.findByCorreo(email)
                .orElseThrow(() -> {
                    log.warn("[HANDLER] Usuario no encontrado: email={}", email);
                    return new NoDataFoundException("No se encontró el usuario con correo " + email);
                });

        log.info("[HANDLER] Usuario encontrado: id={}, correo={}, rol={}",
                userModel.getId(), userModel.getCorreo(),
                Optional.ofNullable(userModel.getRol()).map(r -> r.getNombre()).orElse("sin rol"));

        return UserResponseDto.builder()
                .id(userModel.getId())
                .nombre(userModel.getNombre())
                .correo(userModel.getCorreo())
                .rol(Optional.ofNullable(userModel.getRol()).map(r -> r.getNombre()).orElse(null))
                .fechaCreacion(Optional.ofNullable(userModel.getCreadoEn()).map(Object::toString).orElse(null))
                .build();
    }
}