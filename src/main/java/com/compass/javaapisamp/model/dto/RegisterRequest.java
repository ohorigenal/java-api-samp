package com.compass.javaapisamp.model.dto;

import com.compass.javaapisamp.controller.validator.DateString;
import com.compass.javaapisamp.controller.validator.LocationNumber;
import com.compass.javaapisamp.controller.validator.WeatherNumber;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @JsonProperty("location_id")
    @LocationNumber
    private int locationId;

    @JsonProperty("date")
    @DateString
    private String date;

    @JsonProperty("weather")
    @WeatherNumber
    private int weather;

    @JsonProperty("comment")
    private String comment;
}
