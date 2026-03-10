package com.pragma.usuarios.application.handler;

import com.pragma.usuarios.application.dto.request.LoginRequestDto;
import com.pragma.usuarios.application.dto.response.LoginResponseDto;

public interface IAuthHandler {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
