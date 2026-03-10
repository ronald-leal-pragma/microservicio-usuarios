package com.pragma.usuarios.domain.api;

public interface IAuthServicePort {
    String login(String correo, String clave);
}
