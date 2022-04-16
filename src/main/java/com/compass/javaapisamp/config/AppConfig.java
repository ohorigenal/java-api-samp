package com.compass.javaapisamp.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

    @Value("${exapi.timeout:5000}")
    private int timeout;

    @Value("${api.restTemplate.readTimeout:5000}")
    private int readTimeout;

    @Value("${api.restTemplate.connectTimeout:5000}")
    private int connectTimeout;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofMillis(connectTimeout))
            .setReadTimeout(Duration.ofMillis(readTimeout))
            .build();
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        HttpClient client = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
            .responseTimeout(Duration.ofMillis(timeout))
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(timeout, TimeUnit.MILLISECONDS))
                    .addHandlerLast(new WriteTimeoutHandler(timeout, TimeUnit.MILLISECONDS)));

        return builder
            .clientConnector(new ReactorClientHttpConnector(client))
            .build();
    }
}
