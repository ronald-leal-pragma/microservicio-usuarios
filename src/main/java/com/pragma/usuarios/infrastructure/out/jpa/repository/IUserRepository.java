package com.pragma.usuarios.infrastructure.out.jpa.repository;

import com.pragma.usuarios.infrastructure.out.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByCorreo(String correo);
    boolean existsByDocumentoDeIdentidad(String documentoDeIdentidad);
    Optional<UserEntity> findByCorreo(String correo);
}
