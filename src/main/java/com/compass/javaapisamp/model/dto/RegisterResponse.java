package com.compass.javaapisamp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RegisterResponse {
    @JsonProperty("message")
    private final String message = "weather registered.";
}
