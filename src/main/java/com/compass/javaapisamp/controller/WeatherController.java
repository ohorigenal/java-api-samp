package com.compass.javaapisamp.controller;

import com.compass.javaapisamp.controller.validator.DateString;
import com.compass.javaapisamp.model.WeatherEnum;
import com.compass.javaapisamp.model.dto.RegisterRequest;
import com.compass.javaapisamp.model.dto.RegisterResponse;
import com.compass.javaapisamp.model.dto.WeatherResponse;
import com.compass.javaapisamp.model.entity.Weather;
import com.compass.javaapisamp.model.exception.APIException;
import com.compass.javaapisamp.model.exception.Errors;
import com.compass.javaapisamp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class WeatherController {

    private final WeatherService weatherService;

    @PostMapping("/register")
    public RegisterResponse register(
            @Validated @RequestBody RegisterRequest request,
            BindingResult result
    ) {
        if(result.hasErrors()) {
            log.info(result.getAllErrors().toString());
            throw new APIException(Errors.INVALID_REQUEST);
        }

        log.info("register endpoint. " + request);

        weatherService.register(request);

        return new RegisterResponse();
    }

    @GetMapping("/get/{locationId}/{date}")
    public WeatherResponse getWeather(
            @PathVariable(name = "locationId") int locationId,
            @PathVariable(name = "date") @DateString String date
    ) {
        log.info("get weather endpoint. location_id: " + locationId + ", date:" + date);

        Weather w = weatherService.getWeather(date, locationId);

        return new WeatherResponse(
            w.getLocation().getCity(),
            w.getDate(),
            WeatherEnum.getWeatherString(w.getWeather()),
            w.getComment()
        );
    }
}
