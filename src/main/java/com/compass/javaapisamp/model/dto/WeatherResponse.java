package com.compass.javaapisamp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WeatherResponse {
    @JsonProperty("location")
    private final String location;
    @JsonProperty("date")
    private final String date;
    @JsonProperty("weather")
    private final String weather;
    @JsonProperty("comment")
    private final String comment;
}
