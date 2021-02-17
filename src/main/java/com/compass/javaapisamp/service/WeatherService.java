package com.compass.javaapisamp.service;

import com.compass.javaapisamp.infrastructure.ExAPI;
import com.compass.javaapisamp.model.dto.ExAPIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final ExAPI exAPI;

    public Mono<ExAPIResponse> getExWeather() {
        log.info("getExWeather service.");
        return exAPI.getExWeather();
    }
}
