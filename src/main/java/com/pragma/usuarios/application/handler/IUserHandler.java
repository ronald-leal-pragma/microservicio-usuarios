package com.pragma.usuarios.application.handler;

import com.pragma.usuarios.application.dto.request.ClientRequestDto;
import com.pragma.usuarios.application.dto.request.EmployeeRequestDto;
import com.pragma.usuarios.application.dto.request.UserRequestDto;
import com.pragma.usuarios.application.dto.response.UserCreatedResponseDto;
import com.pragma.usuarios.application.dto.response.UserResponseDto;

public interface IUserHandler {
    UserCreatedResponseDto savePropietario(UserRequestDto userRequestDto);
    UserCreatedResponseDto saveEmployee(EmployeeRequestDto employeeRequestDto);
    UserCreatedResponseDto saveClient(ClientRequestDto clientRequestDto);
    UserResponseDto getUserById(Long id);
    UserResponseDto getUserByEmail(String email);
}
