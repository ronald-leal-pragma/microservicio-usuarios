package com.pragma.usuarios.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequestDto {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un correo electrónico válido")
    private String correo;

    @NotBlank(message = "La clave es obligatoria")
    private String clave;
}
