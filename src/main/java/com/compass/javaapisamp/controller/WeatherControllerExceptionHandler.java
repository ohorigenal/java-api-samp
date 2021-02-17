package com.compass.javaapisamp.controller;

import com.compass.javaapisamp.model.dto.ErrorResponse;
import com.compass.javaapisamp.model.exception.APIException;
import com.compass.javaapisamp.model.exception.Errors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class WeatherControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        log.error(e.getMessage());

        return new ResponseEntity<>(
                new ErrorResponse(Errors.INTERNAL_SERVER_ERROR.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(APIException e) {
        if(e.getError().getStatus().is4xxClientError()) {
            log.debug(e.getMessage());
        }
        return new ResponseEntity<>(
                new ErrorResponse(e.getError().getMessage()),
                e.getError().getStatus()
        );
    }

    /*
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }*/
}
