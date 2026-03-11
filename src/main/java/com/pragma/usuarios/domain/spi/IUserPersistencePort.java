package com.pragma.usuarios.domain.spi;

import com.pragma.usuarios.domain.model.UserModel;

import java.util.Optional;

public interface IUserPersistencePort {
    UserModel saveUser(UserModel userModel);

    Optional<UserModel> findByCorreo(String correo);

    Optional<UserModel> findById(Long id);

    boolean existsByCorreo(String correo);

    boolean existsByDocumentoDeIdentidad(String documento);

}
