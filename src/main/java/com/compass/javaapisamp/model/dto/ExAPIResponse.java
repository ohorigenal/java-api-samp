package com.compass.javaapisamp.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExAPIResponse {
    @JsonProperty("consolidated_weather")
    private List<MetaWeather> weather;
    @JsonProperty("title")
    private String title;
    @JsonProperty("timezone")
    private String timezone;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MetaWeather {
        @JsonProperty("weather_state_name")
        private String stateName;
        @JsonProperty("applicable_date")
        private String date;
        @JsonProperty("wind_speed")
        private double windSpeed;
        @JsonProperty("air_pressure")
        private double airPressure;
        @JsonProperty("humidity")
        private int humidity;
    }
}
