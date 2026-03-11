package com.pragma.usuarios.infrastructure.out.jpa.adapter;

import com.pragma.usuarios.domain.model.UserModel;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import com.pragma.usuarios.infrastructure.out.jpa.entity.UserEntity;
import com.pragma.usuarios.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.pragma.usuarios.infrastructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    @Override
    public UserModel saveUser(UserModel userModel) {
        log.debug("[JPA ADAPTER] Preparando guardado de usuario con correo={}", userModel.getCorreo());

        log.debug("[JPA ADAPTER] Mapeando modelo a entidad");
        UserEntity userEntity = userEntityMapper.toEntity(userModel);

        log.debug("[JPA ADAPTER] Persistiendo en base de datos");
        UserEntity saved = userRepository.save(userEntity);

        log.info("[JPA ADAPTER] Usuario guardado exitosamente con correo={}", userModel.getCorreo());
        return userEntityMapper.toModel(saved);
    }

    @Override
    public boolean existsByCorreo(String correo) {
        return userRepository.existsByCorreo(correo);
    }

    @Override
    public boolean existsByDocumentoDeIdentidad(String documento) {
        return userRepository.existsByDocumentoDeIdentidad(documento);
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
