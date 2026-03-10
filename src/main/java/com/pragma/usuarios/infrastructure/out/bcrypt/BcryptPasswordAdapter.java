package com.pragma.usuarios.infrastructure.out.bcrypt;

import com.pragma.usuarios.domain.spi.IPasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class BcryptPasswordAdapter implements IPasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(String rawPassword) {
        log.debug("[BCRYPT] Encriptando contraseña");
        String encoded = passwordEncoder.encode(rawPassword);
        log.debug("[BCRYPT] Contraseña encriptada exitosamente");
        return encoded;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        log.debug("[BCRYPT] Verificando contraseña");
        boolean result = passwordEncoder.matches(rawPassword, encodedPassword);
        log.debug("[BCRYPT] Verificación de contraseña: {}", result ? "correcta" : "incorrecta");
        return result;
    }
}
