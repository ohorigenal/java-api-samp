package com.compass.javaapisamp.controller;

import com.compass.javaapisamp.model.dto.ExAPIResponse;
import com.compass.javaapisamp.service.ExAPIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ExAPIController {

    private final ExAPIService exAPIService;

    @GetMapping("/get/apidata")
    public Mono<ExAPIResponse> getExAPIData() {

        log.info("api data endpoint.");

        return exAPIService.getExWeather();
    }
}
