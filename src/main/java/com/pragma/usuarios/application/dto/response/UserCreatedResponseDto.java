package com.pragma.usuarios.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreatedResponseDto {
    private Long id;
    private String nombre;
    @JsonProperty("email")
    private String correo;
    @JsonProperty("creadoEn")
    private String creadoEn;
}
