package com.compass.javaapisamp.integration;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ScenarioTest {

    @Value("${integration.url}")
    private String url;

    @Test
    @Order(1)
    void register_new() throws Exception {
        String reqJson = "{\"location_id\":1,\"date\":\"20200303\",\"weather\":2,\"comment\":\"scenario test new comment\"}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .timeout(Duration.ofSeconds(20))
                .header("Auth-Token", "auth-token")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(reqJson))
                .build();

        HttpResponse<String> response = newClient().send(request, HttpResponse.BodyHandlers.ofString());

        String expected = "{\"message\":\"weather registered.\"}";
        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("X-Request-ID").isPresent());
        JSONAssert.assertEquals(expected, response.body(), JSONCompareMode.STRICT);
    }

    @Test
    @Order(2)
    void getWeather_new() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/get/1/20200303"))
                .timeout(Duration.ofSeconds(20))
                .header("Auth-Token", "auth-token")
                .GET().build();

        HttpResponse<String> response = newClient().send(request, HttpResponse.BodyHandlers.ofString());

        String expected = "{\"location\":\"新宿\",\"date\":\"20200303\",\"weather\":\"Cloudy\",\"comment\":\"scenario test new comment\"}";
        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("X-Request-ID").isPresent());
        JSONAssert.assertEquals(expected, response.body(), JSONCompareMode.STRICT);
    }

    @Test
    @Order(3)
    void register_update() throws Exception {
        String reqJson = "{\"location_id\":1,\"date\":\"20200303\",\"weather\":3,\"comment\":\"scenario test update comment\"}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .timeout(Duration.ofSeconds(20))
                .header("Auth-Token", "auth-token")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(reqJson))
                .build();

        HttpResponse<String> response = newClient().send(request, HttpResponse.BodyHandlers.ofString());

        String expected = "{\"message\":\"weather registered.\"}";
        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("X-Request-ID").isPresent());
        JSONAssert.assertEquals(expected, response.body(), JSONCompareMode.STRICT);
    }

    @Test
    @Order(4)
    void getWeather_update() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/get/1/20200303"))
                .timeout(Duration.ofSeconds(20))
                .header("Auth-Token", "auth-token")
                .GET().build();

        HttpResponse<String> response = newClient().send(request, HttpResponse.BodyHandlers.ofString());

        String expected = "{\"location\":\"新宿\",\"date\":\"20200303\",\"weather\":\"Rainy\",\"comment\":\"scenario test update comment\"}";
        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("X-Request-ID").isPresent());
        JSONAssert.assertEquals(expected, response.body(), JSONCompareMode.STRICT);
    }

    private HttpClient newClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
    }
}
