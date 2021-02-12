package com.compass.javaapisamp.infrastructure;

import com.compass.javaapisamp.model.dto.ExAPIResponse;
import com.compass.javaapisamp.model.exception.APIException;
import com.compass.javaapisamp.model.exception.Errors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExAPI {
    private final RestTemplate restTemplate;
    private final WebClient webClient;

    @Value("${exapi.url}")
    private String url;

    public Mono<ExAPIResponse> getExWeather() {
        log.debug("getExWeather start.");

        return webClient
                .get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                //.onStatus(HttpStatus::is4xxClientError, )
                .bodyToMono(ExAPIResponse.class)
                .onErrorMap(e -> new APIException(Errors.INTERNAL_SERVER_ERROR.append(e.getMessage())));/*onErrorResume(WebClientResponseException.class,
                        ex -> ex.getStatusCode().is4xxClientError() || ex.getStatusCode().is5xxServerError()
                                ? Mono.empty() : Mono.error(ex));*/

    }
}
