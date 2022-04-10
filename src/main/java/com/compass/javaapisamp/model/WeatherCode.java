package com.compass.javaapisamp.model;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public enum WeatherCode {
    SUNNY(1, "Sunny"),
    CLOUDY(2, "Cloudy"),
    RAINY(3, "Rainy"),
    SNOWY(4, "Snowy");

    private int id;
    private String name;

    public static String getWeatherString(int id) {
        for(WeatherCode w: values()){
            if(w.id == id) {
                return w.name;
            }
        }
        return "";
    }

    public static boolean isExist(int id) {
        for(WeatherCode w: values()){
            if(w.id == id) {
                return true;
            }
        }
        return false;
    }

    public static String csvValues() {
        List<String> list = new ArrayList<>();
        for(WeatherCode w: values()) {
            list.add(w.id + ":" + w.name);
        }
        return String.join(",", list);
    }
}
