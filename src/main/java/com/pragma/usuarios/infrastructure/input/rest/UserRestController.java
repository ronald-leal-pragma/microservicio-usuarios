package com.pragma.usuarios.infrastructure.input.rest;

import com.pragma.usuarios.application.dto.request.UserRequestDto;
import com.pragma.usuarios.application.handler.IUserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @ApiResponse(responseCode = "201", description = "Propietario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe", content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<Void> savePropietario(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("[REST] POST /user/ - Crear propietario: correo={}", userRequestDto.getCorreo());
        userHandler.savePropietario(userRequestDto);
        log.info("[REST] Propietario creado exitosamente: correo={}", userRequestDto.getCorreo());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
