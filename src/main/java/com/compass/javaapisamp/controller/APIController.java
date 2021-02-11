package com.compass.javaapisamp.controller;

import com.compass.javaapisamp.model.dto.ExAPIResponse;
import com.compass.javaapisamp.model.dto.RegisterRequest;
import com.compass.javaapisamp.model.dto.RegisterResponse;
import com.compass.javaapisamp.model.dto.WeatherResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class APIController {

    @PostMapping("/register")
    public RegisterResponse register(
            @RequestBody RegisterRequest request
    ) {
        return new RegisterResponse();
    }

    @PostMapping("/get/{locationId}/{date}")
    public WeatherResponse getWeather(
            @PathVariable(name = "locationId", required = true) int locationId,
            @PathVariable(name = "date", required = true) String date
    ) {
        return null;
    }

    @GetMapping("/get/apidate")
    public ExAPIResponse getExAPIData() {
        return null;
    }
}
