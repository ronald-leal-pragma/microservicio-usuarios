package com.pragma.usuarios.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedResponseDto {
    private Long id;
    private String nombre;
    @JsonProperty("email")
    private String correo;
    @JsonProperty("creadoEn")
    private String creadoEn;
}
