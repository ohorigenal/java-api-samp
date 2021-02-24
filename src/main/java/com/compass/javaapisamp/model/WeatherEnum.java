package com.compass.javaapisamp.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum WeatherEnum {
    SUNNY(1, "Sunny"),
    CLOUDY(2, "Cloudy"),
    RAINY(3, "Rainy"),
    SNOWY(4, "Snowy");

    private int id;
    private String name;

    public static String getWeatherString(int id) {
        for(WeatherEnum w: values()){
            if(w.id == id) {
                return w.name;
            }
        }
        return "";
    }
}
