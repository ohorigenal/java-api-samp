package com.compass.javaapisamp.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class WeatherID implements Serializable {
    private String date;
    private int location_id;
}
