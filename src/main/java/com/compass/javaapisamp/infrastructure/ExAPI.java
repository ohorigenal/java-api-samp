package com.compass.javaapisamp.infrastructure;

import com.compass.javaapisamp.model.dto.ExAPIResponse;
import com.compass.javaapisamp.model.exception.APIException;
import com.compass.javaapisamp.model.exception.Errors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExAPI {
    private final WebClient webClient;

    @Value("${exapi.url}")
    private String url;

    public Mono<ExAPIResponse> getExWeather() {
        log.info("getExWeather infrastructure.");

        return webClient
                .get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ExAPIResponse.class)
                .doOnError(WebClientResponseException.class,
                        e -> {
                            log.error(e.getMessage());
                            throw new APIException(Errors.INTERNAL_SERVER_ERROR.append(e.getMessage()));
                        });
    }
}
