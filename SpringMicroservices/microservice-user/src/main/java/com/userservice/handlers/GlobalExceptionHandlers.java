package com.userservice.handlers;

import com.userservice.exceptions.AuthException;
import com.userservice.exceptions.JwtValidationException;
import com.userservice.model.dto.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlers {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ExceptionResponse internalServerError(RuntimeException ex){
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return buildExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse authHandler(AuthException ex){
        log.info("Authentication Failed: {}", ex.getMessage(), ex);
        return buildExceptionResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(JwtValidationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse jwtValidationHandler(JwtValidationException ex){
        log.info("JWT validation failed: {}", ex.getMessage(), ex);
        return buildExceptionResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    public ExceptionResponse buildExceptionResponse(HttpStatus httpStatus, String exceptionMessage){
        return ExceptionResponse.builder()
                .status(httpStatus.value())
                .message(exceptionMessage)
                .build();
    }
}

