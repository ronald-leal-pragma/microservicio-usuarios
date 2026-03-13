package com.pragma.usuarios.infrastructure.configuration;

import com.pragma.usuarios.infrastructure.out.jpa.entity.RolEntity;
import com.pragma.usuarios.infrastructure.out.jpa.entity.UserEntity;
import com.pragma.usuarios.infrastructure.out.jpa.repository.IRolRepository;
import com.pragma.usuarios.infrastructure.out.jpa.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InitialDataLoaderTest {

    @Mock
    private IRolRepository rolRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ApplicationArguments applicationArguments;

    private InitialDataLoader initialDataLoader;

    @BeforeEach
    void setUp() {
        initialDataLoader = new InitialDataLoader(rolRepository, userRepository, passwordEncoder);
    }

    @Test
    void runShouldSeedRolesAndAdminWhenMissing() {
        when(rolRepository.existsById(1L)).thenReturn(false);
        when(rolRepository.existsById(2L)).thenReturn(false);
        when(rolRepository.existsById(3L)).thenReturn(false);

        when(userRepository.existsByCorreo("admin@pragma.com")).thenReturn(false);
        when(rolRepository.findById(1L)).thenReturn(Optional.of(new RolEntity(1L, "ADMINISTRADOR", "Admin")));
        when(passwordEncoder.encode("Admin@1234")).thenReturn("encoded-pass");

        initialDataLoader.run(applicationArguments);

        verify(rolRepository, times(3)).save(any(RolEntity.class));

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("admin@pragma.com", userCaptor.getValue().getCorreo());
        assertEquals("encoded-pass", userCaptor.getValue().getClave());
        assertNotNull(userCaptor.getValue().getRol());
    }

    @Test
    void runShouldNotCreateAdminWhenAlreadyExists() {
        when(rolRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsByCorreo("admin@pragma.com")).thenReturn(true);

        initialDataLoader.run(applicationArguments);

        verify(userRepository, never()).save(any(UserEntity.class));
    }
}

