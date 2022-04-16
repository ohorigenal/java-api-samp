package com.compass.javaapisamp.repository;

import lombok.Getter;

@Getter
class RetryableException extends RuntimeException {

    public RetryableException(Throwable cause) {
        super(cause);
    }
}
