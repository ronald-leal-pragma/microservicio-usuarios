package com.pragma.usuarios.application.handler;

import com.pragma.usuarios.application.dto.request.UserRequestDto;
import com.pragma.usuarios.application.dto.response.UserResponseDto;

public interface IUserHandler {
    void savePropietario(UserRequestDto userRequestDto);
    UserResponseDto getUserById(Long id);
}
