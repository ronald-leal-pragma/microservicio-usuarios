package com.pragma.usuarios.infrastructure.exceptionhandler;

import com.pragma.usuarios.domain.exception.DomainException;
import com.pragma.usuarios.infrastructure.exception.NoDataFoundException;
import com.pragma.usuarios.infrastructure.exception.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    // --- CONSTANTES DE DESCRIPCIÓN HTTP ---
    private static final String ERROR_NOT_FOUND = "Not Found";
    private static final String ERROR_CONFLICT = "Conflict";
    private static final String ERROR_BAD_REQUEST = "Bad Request";
    private static final String ERROR_FORBIDDEN = "Forbidden";

    // --- CONSTANTES DE CÓDIGOS DE ERROR ---
    private static final String CODE_USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
    private static final String CODE_VALIDATION_ERROR = "VALIDATION_ERROR";
    private static final String CODE_PERMISSION_DENIED = "PERMISSION_DENIED";

    // --- CONSTANTES DE MENSAJES PERSONALIZADOS ---
    private static final String MSG_INVALID_JSON = "El formato del cuerpo de la petición es inválido";
    private static final String MSG_INVALID_PARAM_TYPE = "Tipo de parámetro inválido: ";
    private static final String MSG_VALIDATION_FAILED = "Validación fallida: ";
    private static final String MSG_ACCESS_DENIED = "No tienes permisos suficientes para acceder a este recurso.";
    private static final String DETAILS_ACCESS_DENIED = "El recurso solicitado requiere permisos adicionales.";

    // --- CONSTANTES DE FORMATO PARA VALIDACIONES ---
    private static final String FIELD_PREFIX = "El campo '";
    private static final String FIELD_SUFFIX = "': ";
    private static final String FIELD_SEPARATOR = "; ";

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoDataFoundException(
            NoDataFoundException ex, HttpServletRequest request) {
        log.warn("[EXCEPTION] 404 - Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDto.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .error(ERROR_NOT_FOUND)
                        .message(ex.getMessage())
                        .timestamp(Instant.now().toString())
                        .path(request.getRequestURI())
                        .build());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyExistsException() {
        log.warn("[EXCEPTION] 409 - Usuario ya existe");
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponseDto.builder()
                        .status(HttpStatus.CONFLICT.value())
                        .error(ERROR_CONFLICT)
                        .message(ExceptionResponse.USER_ALREADY_EXISTS.getMessage())
                        .code(CODE_USER_ALREADY_EXISTS)
                        .build());
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponseDto> handleDomainException(
            DomainException ex) {
        log.warn("[EXCEPTION] 400 - Validación de dominio: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(ERROR_BAD_REQUEST)
                        .message(ex.getMessage())
                        .code(CODE_VALIDATION_ERROR)
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        String combinedMessage = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String field = ((FieldError) error).getField();
                    return FIELD_PREFIX + field + FIELD_SUFFIX + error.getDefaultMessage();
                })
                .collect(Collectors.joining(FIELD_SEPARATOR));

        log.warn("[EXCEPTION] 400 - Validación de campos: {}", combinedMessage);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(ERROR_BAD_REQUEST)
                        .message(MSG_VALIDATION_FAILED + combinedMessage)
                        .code(CODE_VALIDATION_ERROR)
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        log.warn("[EXCEPTION] 400 - JSON no legible: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(ERROR_BAD_REQUEST)
                        .message(MSG_INVALID_JSON)
                        .code(CODE_VALIDATION_ERROR)
                        .build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        log.warn("[EXCEPTION] 400 - Tipo de argumento inválido: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(ERROR_BAD_REQUEST)
                        .message(MSG_INVALID_PARAM_TYPE + ex.getName())
                        .code(CODE_VALIDATION_ERROR)
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(
            AccessDeniedException ex) {
        log.warn("[EXCEPTION] 403 - Acceso denegado: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponseDto.builder()
                        .status(HttpStatus.FORBIDDEN.value())
                        .error(ERROR_FORBIDDEN)
                        .message(MSG_ACCESS_DENIED)
                        .code(CODE_PERMISSION_DENIED)
                        .details(DETAILS_ACCESS_DENIED)
                        .build());
    }
}