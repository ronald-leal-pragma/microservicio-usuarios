package com.pragma.usuarios.application.handler;

import com.pragma.usuarios.application.dto.request.UserRequestDto;
import com.pragma.usuarios.application.dto.response.UserCreatedResponseDto;
import com.pragma.usuarios.application.dto.response.UserResponseDto;

public interface IUserHandler {
    UserCreatedResponseDto savePropietario(UserRequestDto userRequestDto);
    UserResponseDto getUserById(Long id);
}
