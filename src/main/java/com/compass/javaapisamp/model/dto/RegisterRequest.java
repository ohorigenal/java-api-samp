package com.compass.javaapisamp.model.dto;

import com.compass.javaapisamp.controller.validator.DateString;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @JsonProperty("location_id")
    @Min(1)
    private int locationId;

    @JsonProperty("date")
    @Length(min = 8, max = 8) @DateString
    private String date;

    @JsonProperty("weather")
    @Min(1) @Max(4)
    private int weather;

    @JsonProperty("comment")
    private String comment;
}
