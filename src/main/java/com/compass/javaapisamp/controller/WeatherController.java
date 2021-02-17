package com.compass.javaapisamp.controller;

import com.compass.javaapisamp.model.dto.ExAPIResponse;
import com.compass.javaapisamp.model.dto.RegisterRequest;
import com.compass.javaapisamp.model.dto.RegisterResponse;
import com.compass.javaapisamp.model.dto.WeatherResponse;
import com.compass.javaapisamp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @PostMapping("/register")
    public RegisterResponse register(
            @RequestBody RegisterRequest request
    ) {
        return new RegisterResponse();
    }

    @PostMapping("/get/{locationId}/{date}")
    public WeatherResponse getWeather(
            @PathVariable(name = "locationId") int locationId,
            @PathVariable(name = "date") String date
    ) {
        return null;
    }

    @GetMapping("/get/apidata")
    public Mono<ExAPIResponse> getExAPIData() {
        log.info("apidata endpoint.");
        return weatherService.getExWeather();
    }
}
