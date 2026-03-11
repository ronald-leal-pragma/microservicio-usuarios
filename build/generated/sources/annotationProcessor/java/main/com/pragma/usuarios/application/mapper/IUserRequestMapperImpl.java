package com.pragma.usuarios.application.mapper;

import com.pragma.usuarios.application.dto.request.UserRequestDto;
import com.pragma.usuarios.domain.model.UserModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-11T16:41:49-0500",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 21.0.9 (Eclipse Adoptium)"
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
}
