package com.pragma.usuarios.domain.api;

import com.pragma.usuarios.domain.model.UserModel;

public interface IUserServicePort {
    UserModel savePropietario(UserModel userModel);
    UserModel saveEmployee(UserModel userModel);
    UserModel saveClient(UserModel userModel);
}
