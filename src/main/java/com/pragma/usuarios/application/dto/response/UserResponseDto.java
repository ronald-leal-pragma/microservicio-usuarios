package com.pragma.usuarios.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String nombre;
    @JsonProperty("email")
    private String correo;
    private String rol;
    @JsonProperty("fecha_creacion")
    private String fechaCreacion;
}
