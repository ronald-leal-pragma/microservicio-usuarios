package com.pragma.usuarios.infrastructure.exception;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException() {
        super("No se encontraron datos para la petición solicitada");
    }

    public NoDataFoundException(String message) {
        super(message);
    }
}
