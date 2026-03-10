package com.pragma.usuarios.infrastructure.configuration;

import com.pragma.usuarios.infrastructure.out.jpa.entity.RolEntity;
import com.pragma.usuarios.infrastructure.out.jpa.entity.UserEntity;
import com.pragma.usuarios.infrastructure.out.jpa.repository.IRolRepository;
import com.pragma.usuarios.infrastructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitialDataLoader implements ApplicationRunner {

    private final IRolRepository rolRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        seedRoles();
        seedAdminUser();
    }

    private void seedRoles() {
        if (!rolRepository.existsById(1L)) {
            rolRepository.save(new RolEntity(1L, "ADMINISTRADOR", "Administrador del sistema"));
            log.info("[SEED] Rol ADMINISTRADOR creado");
        }
        if (!rolRepository.existsById(2L)) {
            rolRepository.save(new RolEntity(2L, "PROPIETARIO", "Propietario de restaurante"));
            log.info("[SEED] Rol PROPIETARIO creado");
        }
        if (!rolRepository.existsById(3L)) {
            rolRepository.save(new RolEntity(3L, "CLIENTE", "Cliente del sistema"));
            log.info("[SEED] Rol CLIENTE creado");
        }
    }

    private void seedAdminUser() {
        String adminCorreo = "admin@pragma.com";
        if (!userRepository.existsByCorreo(adminCorreo)) {
            RolEntity rolAdmin = rolRepository.findById(1L).orElseThrow();
            UserEntity admin = new UserEntity();
            admin.setNombre("Admin");
            admin.setApellido("Pragma");
            admin.setDocumentoDeIdentidad("0000000001");
            admin.setCelular("3000000000");
            admin.setFechaNacimiento(LocalDate.of(1990, 1, 1));
            admin.setCorreo(adminCorreo);
            admin.setClave(passwordEncoder.encode("Admin@1234"));
            admin.setRol(rolAdmin);
            userRepository.save(admin);
            log.info("[SEED] Usuario ADMINISTRADOR creado: correo={}", adminCorreo);
        }
    }
}
