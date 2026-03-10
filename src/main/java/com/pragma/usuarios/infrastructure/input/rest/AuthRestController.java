package com.pragma.usuarios.infrastructure.input.rest;

import com.pragma.usuarios.application.dto.request.LoginRequestDto;
import com.pragma.usuarios.application.dto.response.LoginResponseDto;
import com.pragma.usuarios.application.handler.IAuthHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Operaciones de autenticación")
public class AuthRestController {

    private final IAuthHandler authHandler;

    @Operation(summary = "Login",
               description = "Autentica al usuario y retorna un token JWT con claims: id, rol y subject (correo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso",
                         content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas o mal formadas", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        log.info("[REST] POST /auth/login - correo={}", loginRequestDto.getCorreo());
        LoginResponseDto response = authHandler.login(loginRequestDto);
        log.info("[REST] Login exitoso para correo={}", loginRequestDto.getCorreo());
        return ResponseEntity.ok(response);
    }
}
