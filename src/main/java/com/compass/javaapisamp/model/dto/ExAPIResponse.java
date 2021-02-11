package com.compass.javaapisamp.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExAPIResponse {
    @JsonProperty("consolidated_weather")
    private final MetaWeather weather;
    @JsonProperty("title")
    private final String title;
    @JsonProperty("timezone")
    private final String timezone;

    @AllArgsConstructor
    public static class MetaWeather {
        @JsonProperty("weather_state_name")
        private final String stateName;
        @JsonProperty("applicable_date")
        private final String date;
        @JsonProperty("wind_speed")
        private final float windSpeed;
        @JsonProperty("air_pressure")
        private final float airPressure;
        @JsonProperty("humidity")
        private final int humidity;
    }
}
