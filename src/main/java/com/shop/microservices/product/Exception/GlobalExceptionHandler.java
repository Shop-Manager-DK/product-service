package com.shop.microservices.product.exception;

import com.shop.microservices.product.Utils.ErrorMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GlobalExceptionHandler is a centralized exception handler for the entire Spring MVC application.
 * It handles various exceptions thrown by controllers and creates consistent error responses.
 * This class leverages {@link ControllerAdvice} to globally handle exceptions and provide
 * structured error messages based on application-specific error codes and messages.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorMessageUtil errorMessageUtil;

    /**
     * Constructor for dependency injection of the {@link ErrorMessageUtil} class.
     * This utility class is used to fetch error messages based on keys from the application's
     * properties files.
     *
     * @param errorMessageUtil The instance of {@link ErrorMessageUtil} to be injected.
     */
    @Autowired
    public GlobalExceptionHandler(ErrorMessageUtil errorMessageUtil) {
        this.errorMessageUtil = errorMessageUtil;
    }

    /**
     * Handles validation errors from request bodies, annotated with {@link javax.validation.Valid}.
     * It collects field-specific error messages and returns them in a structured format.
     *
     * @param ex The {@link MethodArgumentNotValidException} that contains validation errors.
     * @return A {@link ResponseEntity} containing the validation errors in a {@link Map} format.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Validation failed");

        // Extract field-specific validation errors
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));
        response.put("errors", fieldErrors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles validation errors from {@link javax.validation.Valid} annotations on path parameters or query parameters.
     * It collects specific parameter violations and returns them in a structured response.
     *
     * @param ex The {@link ConstraintViolationException} that contains validation errors.
     * @return A {@link ResponseEntity} containing the validation violations in a {@link Map} format.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Validation failed");

        // Extract parameter-specific validation errors
        Map<String, String> violations = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ));
        response.put("errors", violations);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles general exceptions that do not fall under specific exception types.
     * This method returns a generic error response with details from the exception.
     * It utilizes the {@link ErrorMessageUtil} to fetch a user-friendly error message.
     *
     * @param ex      The {@link Exception} instance containing details about the exception.
     * @param request The {@link WebRequest} that triggered the exception.
     * @return A {@link ResponseEntity} containing the generic error details in a {@link Map} format.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", errorMessageUtil.getErrorMessage("generic.error.message"));
        response.put("details", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
