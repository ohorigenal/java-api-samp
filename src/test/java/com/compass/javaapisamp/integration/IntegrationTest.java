package com.compass.javaapisamp.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
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
public class IntegrationTest {

    @Value("${integration.url}")
    private String url;

    @Test
    void register_Success() throws Exception {
        String reqJson = "{\"location_id\":1,\"date\":\"20200202\",\"weather\":1,\"comment\":\"integ test comment\"}";
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
    void register_UnsupportedMediaType() throws Exception {
        String reqJson = "{\"location_id\":1,\"date\":\"20200202\",\"weather\":1,\"comment\":\"integ test comment\"}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .timeout(Duration.ofSeconds(20))
                .header("Auth-Token", "auth-token")
                .POST(HttpRequest.BodyPublishers.ofString(reqJson))
                .build();

        HttpResponse<String> response = newClient().send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(415, response.statusCode());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 20200101, 1, test",
            "1, 2020010, 1, test",
            "1, 202001011, 1, test",
            "1, 20202101, 1, test",
            "1, 20200151, 1, test",
            "1, 20200101, 0, test",
            "1, 20200101, 5, test",
    })
    void register_InvalidRequestError(int location_id, String date, int weather, String comment) throws Exception {
        String reqJsonFmt = "{\"location_id\":%d,\"date\":\"%s\",\"weather\":%d,\"comment\":\"%s\"}";
        String reqJson = String.format(reqJsonFmt, location_id, date, weather, comment);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .timeout(Duration.ofSeconds(20))
                .header("Auth-Token", "auth-token")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(reqJson))
                .build();

        HttpResponse<String> response = newClient().send(request, HttpResponse.BodyHandlers.ofString());

        String expected = "{\"message\":\"invalid request. \"}";
        assertEquals(400, response.statusCode());
        assertTrue(response.headers().firstValue("X-Request-ID").isPresent());
        JSONAssert.assertEquals(expected, response.body(), JSONCompareMode.STRICT);
    }

    @Test
    void register_UnauthorizedError() throws Exception {
        String reqJson = "{\"location_id\":1,\"date\":\"20200202\",\"weather\":1,\"comment\":\"integ test comment\"}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .timeout(Duration.ofSeconds(20))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(reqJson))
                .build();

        HttpResponse<String> response = newClient().send(request, HttpResponse.BodyHandlers.ofString());

        String expected = "{\"message\":\"unauthorized error. \"}";
        assertEquals(401, response.statusCode());
        assertTrue(response.headers().firstValue("X-Request-ID").isPresent());
        JSONAssert.assertEquals(expected, response.body(), JSONCompareMode.STRICT);
    }

    @Test
    void getWeather_Success() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/get/1/20200101"))
                .timeout(Duration.ofSeconds(20))
                .header("Auth-Token", "auth-token")
                .GET().build();

        HttpResponse<String> response = newClient().send(request, HttpResponse.BodyHandlers.ofString());

        String expected = "{\"location\":\"新宿\",\"date\":\"20200101\",\"weather\":\"Sunny\",\"comment\":\"test comment\"}";
        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("X-Request-ID").isPresent());
        JSONAssert.assertEquals(expected, response.body(), JSONCompareMode.STRICT);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 20200101",
            "1, 2020010",
            "1, 202001011",
            "1, 20202101",
            "1, 20200151",
    })
    void getWeather_InvalidRequestError(int locatino_id, String date) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/get/" + locatino_id + "/" + date))
                .timeout(Duration.ofSeconds(20))
                .header("Auth-Token", "auth-token")
                .GET().build();

        HttpResponse<String> response = newClient().send(request, HttpResponse.BodyHandlers.ofString());

        String expected = "{\"message\":\"invalid request. \"}";

        assertEquals(400, response.statusCode());
        assertTrue(response.headers().firstValue("X-Request-ID").isPresent());
        JSONAssert.assertEquals(expected, response.body(), JSONCompareMode.STRICT);
    }

    @Test
    void getWeather_NotFound() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/get/1/20201111"))
                .timeout(Duration.ofSeconds(20))
                .header("Auth-Token", "auth-token")
                .GET().build();

        HttpResponse<String> response = newClient().send(request, HttpResponse.BodyHandlers.ofString());

        String expected = "{\"message\":\"weather not found. \"}";

        assertEquals(400, response.statusCode());
        assertTrue(response.headers().firstValue("X-Request-ID").isPresent());
        JSONAssert.assertEquals(expected, response.body(), JSONCompareMode.STRICT);
    }

    @Test
    void getWeather_Unauthorized() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/get/1/20200101"))
                .timeout(Duration.ofSeconds(20))
                .GET().build();

        HttpResponse<String> response = newClient().send(request, HttpResponse.BodyHandlers.ofString());

        String expected = "{\"message\":\"unauthorized error. \"}";

        assertEquals(401, response.statusCode());
        assertTrue(response.headers().firstValue("X-Request-ID").isPresent());
        JSONAssert.assertEquals(expected, response.body(), JSONCompareMode.STRICT);
    }

    @Test
    void get_apidata() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/get/apidata"))
                .timeout(Duration.ofSeconds(20))
                .header("Auth-Token", "auth-token")
                .GET().build();

        HttpResponse<String> response = newClient().send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("X-Request-ID").isPresent());
        JSONAssert.assertEquals("{\"consolidated_weather\":\"x\", \"title\":\"Tokyo\", \"timezone\":\"Asia/Tokyo\"}",
                response.body(), new CustomComparator(JSONCompareMode.STRICT_ORDER,
                        new Customization("consolidated_weather", new RegularExpressionValueMatcher<>(".+"))
                ));
    }

    @Test
    void get_apidata_Unauthorized() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/get/apidata"))
                .timeout(Duration.ofSeconds(20))
                .GET().build();

        HttpResponse<String> response = newClient().send(request, HttpResponse.BodyHandlers.ofString());

        String expected = "{\"message\":\"unauthorized error. \"}";

        assertEquals(401, response.statusCode());
        assertTrue(response.headers().firstValue("X-Request-ID").isPresent());
        JSONAssert.assertEquals(expected, response.body(), JSONCompareMode.STRICT);
    }

    private HttpClient newClient() {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
    }

}
