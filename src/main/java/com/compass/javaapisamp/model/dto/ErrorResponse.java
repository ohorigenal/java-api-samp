package com.compass.javaapisamp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
    @JsonProperty("message")
    private final String message;
}
