package com.pragma.usuarios.application.mapper;

import com.pragma.usuarios.application.dto.request.ClientRequestDto;
import com.pragma.usuarios.application.dto.request.EmployeeRequestDto;
import com.pragma.usuarios.application.dto.request.UserRequestDto;
import com.pragma.usuarios.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserRequestMapper {
    UserModel toUser(UserRequestDto userRequestDto);
    UserModel toUserFromEmployee(EmployeeRequestDto employeeRequestDto);
    UserModel toUserFromClient(ClientRequestDto clientRequestDto);
}
