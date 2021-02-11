package com.compass.javaapisamp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RegisterRequest {
    @JsonProperty("location_id")
    private final int locationId;
    @JsonProperty("date")
    private final String date;
    @JsonProperty("weather")
    private final int weather;
    @JsonProperty("comment")
    private final String comment;
}
