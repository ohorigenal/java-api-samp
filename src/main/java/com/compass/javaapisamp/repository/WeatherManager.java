package com.compass.javaapisamp.repository;

import com.compass.javaapisamp.model.entity.Weather;
import com.compass.javaapisamp.model.entity.WeatherID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherManager extends JpaRepository<Weather, WeatherID> {

}
