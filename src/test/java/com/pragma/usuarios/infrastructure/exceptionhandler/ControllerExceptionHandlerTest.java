package com.pragma.usuarios.infrastructure.exceptionhandler;

import com.pragma.usuarios.domain.exception.DomainException;
import com.pragma.usuarios.infrastructure.exception.NoDataFoundException;
import com.pragma.usuarios.infrastructure.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new ControllerExceptionHandler();
    }

    @Test
    void handleNoDataFoundExceptionShouldReturnNotFound() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/user/100");

        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleNoDataFoundException(
                new NoDataFoundException("No existe"), request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("No existe", response.getBody().getMessage());
        assertEquals("/user/100", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleUserAlreadyExistsExceptionShouldReturnConflict() {
        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleUserAlreadyExistsException();

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().getStatus());
        assertEquals("USER_ALREADY_EXISTS", response.getBody().getCode());
        assertEquals(ExceptionResponse.USER_ALREADY_EXISTS.getMessage(), response.getBody().getMessage());
    }

    @Test
    void handleDomainExceptionShouldReturnBadRequest() {
        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleDomainException(
                new DomainException("Regla de negocio"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("VALIDATION_ERROR", response.getBody().getCode());
        assertEquals("Regla de negocio", response.getBody().getMessage());
    }

    @Test
    void handleMethodArgumentNotValidExceptionShouldBuildCombinedMessage() throws Exception {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "obj");
        bindingResult.addError(new FieldError("obj", "correo", "Debe ser valido"));
        bindingResult.addError(new FieldError("obj", "clave", "Es obligatoria"));

        Method method = SampleController.class.getDeclaredMethod("sampleMethod", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleMethodArgumentNotValidException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("Validación fallida:"));
        assertTrue(response.getBody().getMessage().contains("correo"));
        assertTrue(response.getBody().getMessage().contains("clave"));
    }

    @Test
    void handleHttpMessageNotReadableExceptionShouldReturnBadRequest() {
        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleHttpMessageNotReadableException(
                new HttpMessageNotReadableException("json invalido"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("El formato del cuerpo de la petición es inválido", response.getBody().getMessage());
    }

    @Test
    void handleMethodArgumentTypeMismatchExceptionShouldReturnBadRequest() {
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                "abc", Long.class, "id", null, null);

        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleMethodArgumentTypeMismatchException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Tipo de parámetro inválido: id", response.getBody().getMessage());
    }

    @Test
    void handleAccessDeniedExceptionShouldReturnForbidden() {
        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleAccessDeniedException(
                new AccessDeniedException("denegado"));

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PERMISSION_DENIED", response.getBody().getCode());
        assertNotNull(response.getBody().getDetails());
    }

    private static class SampleController {
        @SuppressWarnings("unused")
        public void sampleMethod(String value) {
        }
    }
}

