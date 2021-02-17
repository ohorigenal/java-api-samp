package com.compass.javaapisamp.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Errors {

    INVALID_REQUEST("invalid request. ", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST_PARAM("invalid request param. ", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("unauthorized error. ", HttpStatus.UNAUTHORIZED),
    INTERNAL_SERVER_ERROR("internal server error. ", HttpStatus.INTERNAL_SERVER_ERROR);

    private String message;
    private final HttpStatus status;

    public Errors append(String message) {
        this.message += message;
        return this;
    }
}
