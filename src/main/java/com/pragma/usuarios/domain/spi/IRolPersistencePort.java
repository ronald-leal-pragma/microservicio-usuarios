package com.pragma.usuarios.domain.spi;

import com.pragma.usuarios.domain.model.RolModel;

import java.util.Optional;

public interface IRolPersistencePort {
    Optional<RolModel> findByName(String name);

    RolModel save(RolModel rolModel);
}
