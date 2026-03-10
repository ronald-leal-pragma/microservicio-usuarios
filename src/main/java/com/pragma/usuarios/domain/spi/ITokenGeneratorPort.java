package com.pragma.usuarios.domain.spi;

import com.pragma.usuarios.domain.model.UserModel;

public interface ITokenGeneratorPort {
    String generateToken(UserModel userModel);
}
