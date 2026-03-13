package com.pragma.usuarios.application.mapper;

import com.pragma.usuarios.application.dto.request.ClientRequestDto;
import com.pragma.usuarios.application.dto.request.EmployeeRequestDto;
import com.pragma.usuarios.application.dto.request.UserRequestDto;
import com.pragma.usuarios.domain.model.UserModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-13T09:05:20-0500",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class IUserRequestMapperImpl implements IUserRequestMapper {

    @Override
    public UserModel toUser(UserRequestDto userRequestDto) {
        if ( userRequestDto == null ) {
            return null;
        }

        UserModel userModel = new UserModel();

        userModel.setNombre( userRequestDto.getNombre() );
        userModel.setApellido( userRequestDto.getApellido() );
        userModel.setDocumentoDeIdentidad( userRequestDto.getDocumentoDeIdentidad() );
        userModel.setCelular( userRequestDto.getCelular() );
        userModel.setFechaNacimiento( userRequestDto.getFechaNacimiento() );
        userModel.setCorreo( userRequestDto.getCorreo() );
        userModel.setClave( userRequestDto.getClave() );

        return userModel;
    }

    @Override
    public UserModel toUserFromEmployee(EmployeeRequestDto employeeRequestDto) {
        if ( employeeRequestDto == null ) {
            return null;
        }

        UserModel userModel = new UserModel();

        userModel.setNombre( employeeRequestDto.getNombre() );
        userModel.setApellido( employeeRequestDto.getApellido() );
        userModel.setDocumentoDeIdentidad( employeeRequestDto.getDocumentoDeIdentidad() );
        userModel.setCelular( employeeRequestDto.getCelular() );
        userModel.setFechaNacimiento( employeeRequestDto.getFechaNacimiento() );
        userModel.setCorreo( employeeRequestDto.getCorreo() );
        userModel.setClave( employeeRequestDto.getClave() );

        return userModel;
    }

    @Override
    public UserModel toUserFromClient(ClientRequestDto clientRequestDto) {
        if ( clientRequestDto == null ) {
            return null;
        }

        UserModel userModel = new UserModel();

        userModel.setNombre( clientRequestDto.getNombre() );
        userModel.setApellido( clientRequestDto.getApellido() );
        userModel.setDocumentoDeIdentidad( clientRequestDto.getDocumentoDeIdentidad() );
        userModel.setCelular( clientRequestDto.getCelular() );
        userModel.setCorreo( clientRequestDto.getCorreo() );
        userModel.setClave( clientRequestDto.getClave() );

        return userModel;
    }
}
