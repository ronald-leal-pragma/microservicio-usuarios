package com.pragma.usuarios.infrastructure.out.jpa.adapter;

import com.pragma.usuarios.domain.model.UserModel;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import com.pragma.usuarios.infrastructure.exception.UserAlreadyExistsException;
import com.pragma.usuarios.infrastructure.out.jpa.entity.RolEntity;
import com.pragma.usuarios.infrastructure.out.jpa.entity.UserEntity;
import com.pragma.usuarios.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.pragma.usuarios.infrastructure.out.jpa.repository.IRolRepository;
import com.pragma.usuarios.infrastructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IRolRepository rolRepository;
    private final IUserEntityMapper userEntityMapper;

    @Override
    public UserModel saveUser(UserModel userModel) {
        log.info("[JPA ADAPTER] Verificando duplicados para correo={} y documento={}",
                userModel.getCorreo(), userModel.getDocumentoDeIdentidad());

        if (userRepository.existsByCorreo(userModel.getCorreo())) {
            log.warn("[JPA ADAPTER] Correo ya registrado: {}", userModel.getCorreo());
            throw new UserAlreadyExistsException();
        }
        if (userRepository.existsByDocumentoDeIdentidad(userModel.getDocumentoDeIdentidad())) {
            log.warn("[JPA ADAPTER] Documento ya registrado: {}", userModel.getDocumentoDeIdentidad());
            throw new UserAlreadyExistsException();
        }

        Long rolId = userModel.getRol().getId();
        RolEntity rolEntity = rolRepository.findById(rolId)
                .orElseGet(() -> {
                    log.info("[JPA ADAPTER] Rol no encontrado, creando: {}", userModel.getRol().getNombre());
                    RolEntity newRol = new RolEntity();
                    newRol.setId(rolId);
                    newRol.setNombre(userModel.getRol().getNombre());
                    newRol.setDescripcion(userModel.getRol().getDescripcion());
                    return rolRepository.save(newRol);
                });

        UserEntity userEntity = userEntityMapper.toEntity(userModel);
        userEntity.setRol(rolEntity);
        UserEntity saved = userRepository.save(userEntity);
        log.info("[JPA ADAPTER] Usuario guardado exitosamente con correo={}", userModel.getCorreo());
        return userEntityMapper.toModel(saved);
}

    @Override
    public Optional<UserModel> findByCorreo(String correo) {
        log.debug("[JPA ADAPTER] Buscando usuario con correo={}", correo);
        return userRepository.findByCorreo(correo)
                .map(userEntityMapper::toModel);
    }

    @Override
    public Optional<UserModel> findById(Long id) {
        log.debug("[JPA ADAPTER] Buscando usuario con id={}", id);
        return userRepository.findById(id)
                .map(userEntityMapper::toModel);
    }
}
