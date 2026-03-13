package com.pragma.usuarios.domain.exception;

public class ExceptionConstants {
    private ExceptionConstants() {}

    public static final String UNDERAGE_USER_MESSAGE = "El propietario debe ser mayor de edad";
    public static final String INVALID_CREDENTIALS_MESSAGE = "Correo o contraseña incorrectos";
    public static final String USER_NOT_FOUND_MESSAGE = "El usuario no existe";

    public static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";
    public static final Long ROL_ADMINISTRADOR_ID = 1L;

    public static final String ROL_PROPIETARIO = "PROPIETARIO";
    public static final Long ROL_PROPIETARIO_ID = 2L;

    public static final String ROL_CLIENTE = "CLIENTE";
    public static final Long ROL_CLIENTE_ID = 3L;

    public static final String ROL_EMPLEADO = "EMPLEADO";
    public static final Long ROL_EMPLEADO_ID = 4L;

    public static final String USER_EMAIL_ALREADY_EXISTS_MESSAGE = "Ya existe un usuario registrado con este correo";
    public static final String USER_DOCUMENT_ALREADY_EXISTS_MESSAGE = "Ya existe un usuario registrado con este documento de identidad";
}
