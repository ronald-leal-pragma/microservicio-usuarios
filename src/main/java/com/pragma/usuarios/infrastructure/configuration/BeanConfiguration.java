package com.pragma.usuarios.infrastructure.configuration;

import com.pragma.usuarios.domain.api.IAuthServicePort;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.model.RolModel;
import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import com.pragma.usuarios.domain.spi.IRolPersistencePort;
import com.pragma.usuarios.domain.spi.ITokenGeneratorPort;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import com.pragma.usuarios.domain.usecase.AuthUseCase;
import com.pragma.usuarios.domain.usecase.UserUseCase;
import com.pragma.usuarios.infrastructure.out.bcrypt.BcryptPasswordAdapter;
import com.pragma.usuarios.infrastructure.out.jpa.adapter.UserJpaAdapter;
import com.pragma.usuarios.infrastructure.out.jpa.entity.RolEntity;
import com.pragma.usuarios.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.pragma.usuarios.infrastructure.out.jpa.repository.IRolRepository;
import com.pragma.usuarios.infrastructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;
    private final IRolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final ITokenGeneratorPort tokenGeneratorPort;

    @Bean
    public IPasswordEncoderPort passwordEncoderPort() {
        return new BcryptPasswordAdapter(passwordEncoder);
    }

    @Bean
    public IRolPersistencePort rolPersistencePort() {
        return new IRolPersistencePort() {
            @Override
            public Optional<RolModel> findByName(String name) {
                return rolRepository.findAll().stream()
                        .filter(rol -> rol.getNombre().equalsIgnoreCase(name))
                        .findFirst()
                        .map(rol -> new RolModel(rol.getId(), rol.getNombre(), rol.getDescripcion()));
            }

            @Override
            public RolModel save(RolModel rolModel) {
                RolEntity saved = rolRepository.save(new RolEntity(
                        rolModel.getId(),
                        rolModel.getNombre(),
                        rolModel.getDescripcion()
                ));
                return new RolModel(saved.getId(), saved.getNombre(), saved.getDescripcion());
            }
        };
    }

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserJpaAdapter(userRepository, userEntityMapper);
    }

    @Bean
    public IUserServicePort userServicePort() {
        return new UserUseCase(
                userPersistencePort(),
                passwordEncoderPort(),
                rolPersistencePort()
        );
    }

    @Bean
    public IAuthServicePort authServicePort() {
        return new AuthUseCase(userPersistencePort(), passwordEncoderPort(), tokenGeneratorPort);
    }
}
