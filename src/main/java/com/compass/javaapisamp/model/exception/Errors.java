package com.compass.javaapisamp.model.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Errors {

    INVALID_REQUEST("invalid request.", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST_PARAM("invalid request param.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("unauthorized error.", HttpStatus.UNAUTHORIZED),
    INTERNAL_SERVER_ERROR("internal server error.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;
    private String append;

    public Errors append(String message) {
        this.append += message;
        return this;
    }
}
