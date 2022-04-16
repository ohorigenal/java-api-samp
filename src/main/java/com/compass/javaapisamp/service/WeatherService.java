package com.compass.javaapisamp.service;

import com.compass.javaapisamp.model.dto.RegisterRequest;
import com.compass.javaapisamp.model.entity.Location;
import com.compass.javaapisamp.model.entity.Weather;
import com.compass.javaapisamp.model.entity.WeatherID;
import com.compass.javaapisamp.model.exception.APIException;
import com.compass.javaapisamp.model.exception.Errors;
import com.compass.javaapisamp.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRepository weatherRepository;

    public void register(RegisterRequest payload) {
        try {
            Weather weather = new Weather(
                payload.getDate(),
                payload.getWeather(),
                new Location(payload.getLocationId(), null),
                payload.getLocationId(),
                payload.getComment()
            );
            weatherRepository.saveWeather(weather);
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            log.error("register service error. " + e);
            throw new APIException(Errors.INTERNAL_SERVER_ERROR);
        }
    }

    public Weather getWeather(String date, int location_id) {
        try {
            return weatherRepository.findById(new WeatherID(date, location_id));
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            log.error("weather service error. " + e);
            throw new APIException(Errors.INTERNAL_SERVER_ERROR);
        }
    }
}
