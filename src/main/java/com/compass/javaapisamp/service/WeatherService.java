package com.compass.javaapisamp.service;

import com.compass.javaapisamp.infrastructure.ExAPI;
import com.compass.javaapisamp.model.dto.ExAPIResponse;
import com.compass.javaapisamp.model.dto.RegisterRequest;
import com.compass.javaapisamp.model.entity.Location;
import com.compass.javaapisamp.model.entity.Weather;
import com.compass.javaapisamp.model.entity.WeatherID;
import com.compass.javaapisamp.model.exception.APIException;
import com.compass.javaapisamp.model.exception.Errors;
import com.compass.javaapisamp.repository.WeatherManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final ExAPI exAPI;

    private final WeatherManager weatherManager;

    public void register(RegisterRequest payload) {
        log.info("register service.");

        try {
            Weather entity = new Weather(
                    payload.getDate(),
                    payload.getWeather(),
                    new Location(payload.getLocationId(), null),
                    payload.getLocationId(),
                    payload.getComment()
            );
            weatherManager.save(entity);
        }catch (Exception e) {
            log.error("register service error.");
            throw new APIException(Errors.INTERNAL_SERVER_ERROR);
        }
    }

    public Weather getWeather(String date, int location_id) {
        log.info("getWeather service.");

        try {
            Optional<Weather> weatherOptional = weatherManager.findById(new WeatherID(date, location_id));
            return weatherOptional.get();
        }catch (NoSuchElementException e) {
            log.info("weather not found. " + e.getMessage());
            throw new APIException(Errors.WEATHER_NOT_FOUND.append(e.getMessage()));
        }catch (Exception e) {
            log.error("weather service error. " + e.getMessage());
            throw new APIException(Errors.INTERNAL_SERVER_ERROR);
        }
    }

    public Mono<ExAPIResponse> getExWeather() {
        log.info("getExWeather service.");
        return exAPI.getExWeather();
    }
}
