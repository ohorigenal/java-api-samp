package com.compass.javaapisamp.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class APIException extends RuntimeException {
    private final Errors error;
}
