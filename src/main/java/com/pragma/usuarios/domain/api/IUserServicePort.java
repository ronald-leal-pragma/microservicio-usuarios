package com.pragma.usuarios.domain.api;

import com.pragma.usuarios.domain.model.UserModel;

public interface IUserServicePort {
    void savePropietario(UserModel userModel);
}
