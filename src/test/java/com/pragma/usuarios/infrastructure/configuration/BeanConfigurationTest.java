package com.pragma.usuarios.infrastructure.configuration;

import com.pragma.usuarios.domain.api.IAuthServicePort;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.model.RolModel;
import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import com.pragma.usuarios.domain.spi.IRolPersistencePort;
import com.pragma.usuarios.domain.spi.ITokenGeneratorPort;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import com.pragma.usuarios.infrastructure.out.jpa.entity.RolEntity;
import com.pragma.usuarios.infrastructure.out.jpa.repository.IRolRepository;
import com.pragma.usuarios.infrastructure.out.jpa.repository.IUserRepository;
import com.pragma.usuarios.infrastructure.out.jpa.mapper.IUserEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeanConfigurationTest {

    @Mock
    private IUserRepository userRepository;
    @Mock
    private IUserEntityMapper userEntityMapper;
    @Mock
    private IRolRepository rolRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ITokenGeneratorPort tokenGeneratorPort;

    private BeanConfiguration beanConfiguration;

    @BeforeEach
    void setUp() {
        beanConfiguration = new BeanConfiguration(
                userRepository,
                userEntityMapper,
                rolRepository,
                passwordEncoder,
                tokenGeneratorPort
        );
    }

    @Test
    void passwordEncoderPortShouldCreateAdapter() {
        IPasswordEncoderPort port = beanConfiguration.passwordEncoderPort();

        assertNotNull(port);
        when(passwordEncoder.encode("123")).thenReturn("encoded");
        assertEquals("encoded", port.encode("123"));
    }

    @Test
    void rolPersistencePortFindByNameShouldIgnoreCase() {
        when(rolRepository.findAll()).thenReturn(List.of(
                new RolEntity(1L, "ADMINISTRADOR", "Admin"),
                new RolEntity(2L, "PROPIETARIO", "Propietario")
        ));

        IRolPersistencePort rolPort = beanConfiguration.rolPersistencePort();
        Optional<RolModel> found = rolPort.findByName("propietario");

        assertTrue(found.isPresent());
        assertEquals(2L, found.get().getId());
    }

    @Test
    void rolPersistencePortSaveShouldMapAndReturnRolModel() {
        RolModel input = new RolModel(3L, "CLIENTE", "Cliente");
        RolEntity saved = new RolEntity(3L, "CLIENTE", "Cliente");
        when(rolRepository.save(any(RolEntity.class))).thenReturn(saved);

        IRolPersistencePort rolPort = beanConfiguration.rolPersistencePort();
        RolModel result = rolPort.save(input);

        assertEquals(3L, result.getId());
        assertEquals("CLIENTE", result.getNombre());
    }

    @Test
    void servicePortsShouldBeCreated() {
        IUserPersistencePort userPersistencePort = beanConfiguration.userPersistencePort();
        IUserServicePort userServicePort = beanConfiguration.userServicePort();
        IAuthServicePort authServicePort = beanConfiguration.authServicePort();

        assertNotNull(userPersistencePort);
        assertNotNull(userServicePort);
        assertNotNull(authServicePort);
    }
}

