package com.pragma.usuarios.infrastructure.out.jpa.mapper;

import com.pragma.usuarios.domain.model.RolModel;
import com.pragma.usuarios.domain.model.UserModel;
import com.pragma.usuarios.infrastructure.out.jpa.entity.RolEntity;
import com.pragma.usuarios.infrastructure.out.jpa.entity.UserEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-12T14:41:27-0500",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class IUserEntityMapperImpl implements IUserEntityMapper {

    @Override
    public UserEntity toEntity(UserModel userModel) {
        if ( userModel == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setRol( rolModelToRolEntity( userModel.getRol() ) );
        userEntity.setId( userModel.getId() );
        userEntity.setNombre( userModel.getNombre() );
        userEntity.setApellido( userModel.getApellido() );
        userEntity.setDocumentoDeIdentidad( userModel.getDocumentoDeIdentidad() );
        userEntity.setCelular( userModel.getCelular() );
        userEntity.setFechaNacimiento( userModel.getFechaNacimiento() );
        userEntity.setCorreo( userModel.getCorreo() );
        userEntity.setClave( userModel.getClave() );
        userEntity.setCreadoEn( userModel.getCreadoEn() );

        return userEntity;
    }

    @Override
    public UserModel toModel(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserModel userModel = new UserModel();

        userModel.setRol( rolEntityToRolModel( userEntity.getRol() ) );
        userModel.setId( userEntity.getId() );
        userModel.setNombre( userEntity.getNombre() );
        userModel.setApellido( userEntity.getApellido() );
        userModel.setDocumentoDeIdentidad( userEntity.getDocumentoDeIdentidad() );
        userModel.setCelular( userEntity.getCelular() );
        userModel.setFechaNacimiento( userEntity.getFechaNacimiento() );
        userModel.setCorreo( userEntity.getCorreo() );
        userModel.setClave( userEntity.getClave() );
        userModel.setCreadoEn( userEntity.getCreadoEn() );

        return userModel;
    }

    protected RolEntity rolModelToRolEntity(RolModel rolModel) {
        if ( rolModel == null ) {
            return null;
        }

        RolEntity rolEntity = new RolEntity();

        rolEntity.setId( rolModel.getId() );
        rolEntity.setNombre( rolModel.getNombre() );
        rolEntity.setDescripcion( rolModel.getDescripcion() );

        return rolEntity;
    }

    protected RolModel rolEntityToRolModel(RolEntity rolEntity) {
        if ( rolEntity == null ) {
            return null;
        }

        RolModel rolModel = new RolModel();

        rolModel.setId( rolEntity.getId() );
        rolModel.setNombre( rolEntity.getNombre() );
        rolModel.setDescripcion( rolEntity.getDescripcion() );

        return rolModel;
    }
}
