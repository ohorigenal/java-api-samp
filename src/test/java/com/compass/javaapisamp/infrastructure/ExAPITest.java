package com.compass.javaapisamp.infrastructure;

import com.compass.javaapisamp.model.dto.ExAPIResponse;
import com.compass.javaapisamp.model.exception.APIException;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ExAPITest {

    @Autowired
    private ExAPI exAPI;

    private static MockWebServer mockWebServer;

    @BeforeAll
    public static void setupAll() throws IOException {
        System.out.println("all");
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    public static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    public void setupEach() {
        ReflectionTestUtils.setField(exAPI, "url", "http://localhost:" + mockWebServer.getPort());
    }

    @Test
    public void normal_test() throws JsonProcessingException {

        ExAPIResponse expected = new ExAPIResponse(
            new ArrayList<>() {
                {
                    add(new ExAPIResponse.MetaWeather(
                            "Clear", "2021-01-29", 10.214677378467085D, 1003D, 37
                    ));
                    add(new ExAPIResponse.MetaWeather(
                            "Clear", "2021-01-30", 8.608168518515868D, 1018.5D, 37
                    ));
                }
            },
            "Tokyo",
            "Asia/Tokyo"
        );

        mockWebServer.enqueue(new MockResponse().setBody(
                "{\"consolidated_weather\":[{\"weather_state_name\":\"Clear\",\"applicable_date\":\"2021-01-29\",\"wind_speed\":10.214677378467085,\"air_pressure\":1003,\"humidity\":37},{\"weather_state_name\":\"Clear\",\"applicable_date\":\"2021-01-30\",\"wind_speed\":8.608168518515868,\"air_pressure\":1018.5,\"humidity\":37}],\"title\":\"Tokyo\",\"timezone\":\"Asia/Tokyo\"}"
        ).addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

        Mono<ExAPIResponse> mono = exAPI.getExWeather();

        StepVerifier.create(mono)
                .expectNextMatches(obj -> {
                    boolean flag = true;
                    List<ExAPIResponse.MetaWeather> expectedList = expected.getWeather();
                    List<ExAPIResponse.MetaWeather> actualList = obj.getWeather();
                    for(int i=0; i<expectedList.size(); i++) {
                        ExAPIResponse.MetaWeather ew = expectedList.get(i);
                        ExAPIResponse.MetaWeather aw = actualList.get(i);
                        if(!(aw.getStateName().equals(ew.getStateName())
                                && aw.getDate().equals(ew.getDate())
                                && aw.getWindSpeed() == ew.getWindSpeed()
                                && aw.getAirPressure() == ew.getAirPressure()
                                && aw.getHumidity() == ew.getHumidity()
                        )) {
                            flag = false;
                            break;
                        }
                    }
                    return flag
                            && expectedList.size() == actualList.size()
                            && expected.getTitle().equals(obj.getTitle())
                            && expected.getTimezone().equals(obj.getTimezone());
                })
                .verifyComplete();
    }

    @Test
    public void error_test() {
        //Mockito.when(webClient.get()).thenThrow(new RuntimeException("api error"));
        mockWebServer.enqueue(new MockResponse().setResponseCode(401));
        Mono<ExAPIResponse> mono = exAPI.getExWeather();
        StepVerifier.create(mono)
                .expectErrorSatisfies(t -> assertTrue(( t instanceof APIException)))
                .verify();
        //APIException e = assertThrows(APIException.class, () -> exAPI.getExWeather());
        //assertEquals(Errors.INTERNAL_SERVER_ERROR, e.getError());
    }

}
