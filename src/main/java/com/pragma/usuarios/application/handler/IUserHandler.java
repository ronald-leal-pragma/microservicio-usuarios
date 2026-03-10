package com.pragma.usuarios.application.handler;

import com.pragma.usuarios.application.dto.request.UserRequestDto;

public interface IUserHandler {
    void savePropietario(UserRequestDto userRequestDto);
}
