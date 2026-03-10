package com.pragma.usuarios.infrastructure.input.rest;

import com.pragma.usuarios.application.dto.request.UserRequestDto;
import com.pragma.usuarios.application.dto.response.UserCreatedResponseDto;
import com.pragma.usuarios.application.dto.response.UserResponseDto;
import com.pragma.usuarios.application.handler.IUserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Operaciones relacionadas con usuarios")
public class UserRestController {

    private final IUserHandler userHandler;

    @Operation(summary = "Crear propietario",
               description = "Crea un nuevo usuario con rol de propietario. Solo puede ser invocado por un ADMINISTRADOR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Propietario creado exitosamente",
                         content = @Content(schema = @Schema(implementation = UserCreatedResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<UserCreatedResponseDto> savePropietario(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("[REST] POST /user/ - Crear propietario: correo={}", userRequestDto.getCorreo());
        UserCreatedResponseDto created = userHandler.savePropietario(userRequestDto);
        log.info("[REST] Propietario creado exitosamente: correo={}", userRequestDto.getCorreo());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Obtener usuario por ID",
               description = "Retorna la información básica de un usuario. Uso interno entre microservicios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                         content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        log.info("[REST] GET /user/{} - Buscar usuario por ID", id);
        UserResponseDto response = userHandler.getUserById(id);
        return ResponseEntity.ok(response);
    }
}
