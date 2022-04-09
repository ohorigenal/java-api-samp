package com.compass.javaapisamp.controller;

import com.compass.javaapisamp.helper.TestHelper;
import com.compass.javaapisamp.model.entity.Location;
import com.compass.javaapisamp.model.entity.Weather;
import com.compass.javaapisamp.model.exception.APIException;
import com.compass.javaapisamp.model.exception.Errors;
import com.compass.javaapisamp.service.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {

    @MockBean
    private WeatherService weatherService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void register_Success() throws Exception {
        String reqJson = "{\"location_id\":1,\"date\":\"20200101\",\"weather\":1,\"comment\":\"test comment\"}";
        String resJson = TestHelper.readClassPathResource("weather/register_success_response.json");

        Mockito.doNothing().when(weatherService).register(any());

        mockMvc.perform(post("/register")
                .header("Auth-Token", "auth-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andExpect(status().isOk())
                .andExpect(content().json(resJson))
                .andExpect(header().exists("X-Request-ID"));
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
    void registerInvalidRequestErrorTest(int location_id, String date, int weather, String comment) throws Exception {
        String reqJsonFmt = "{\"location_id\":%d,\"date\":\"%s\",\"weather\":%d,\"comment\":\"%s\"}";
        String reqJson = String.format(reqJsonFmt, location_id, date, weather, comment);
        String resJson = TestHelper.readClassPathResource("common/invalid_request_response.json");

        Mockito.doNothing().when(weatherService).register(any());

        mockMvc.perform(post("/register")
                .header("Auth-Token", "auth-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(resJson))
                .andExpect(header().exists("X-Request-ID"));
    }

    @Test
    void registerInternalServerErrorTest() throws Exception {
        String reqJson = "{\"location_id\":1,\"date\":\"20200101\",\"weather\":1,\"comment\":\"test comment\"}";
        String resJson = TestHelper.readClassPathResource("common/server_error_response.json");

        Mockito.doThrow(new APIException(Errors.INTERNAL_SERVER_ERROR)).when(weatherService).register(any());

        mockMvc.perform(post("/register")
                .header("Auth-Token", "auth-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(resJson))
                .andExpect(header().exists("X-Request-ID"));
    }

    @Test
    void registerFilterTest() throws Exception {
        String reqJson = "{\"location_id\":1,\"date\":\"20200101\",\"weather\":1,\"comment\":\"test comment\"}";
        String resJson = TestHelper.readClassPathResource("common/unauthorized_error_response.json");

        mockMvc.perform(post("/register")
                .header("X-Request-ID", "AAA")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqJson))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(resJson))
                .andExpect(header().string("X-Request-ID", "AAA"));
    }

    /*
     * GET WEATHER
     */
    @Test
    void getWeatherSuccessTest() throws Exception {
        String resJson = TestHelper.readClassPathResource("weather/get_weather_success_response.json");

        int location_id = 1;
        String date = "20200101";
        Mockito.when(weatherService.getWeather(date, location_id)).thenReturn(
                new Weather(
                        "20200101",
                        1,
                        new Location(1, "東京"),
                        1,
                        "test comment"
                ));

        mockMvc.perform(get("/get/{location_id}/{date}", location_id, date)
                .header("Auth-Token", "auth-token"))
            .andExpect(status().isOk())
            .andExpect(content().json(resJson))
            .andExpect(header().exists("X-Request-ID"));
    }

    @Test
    void getWeatherInvalidRequestErrorTest() throws Exception {
        String resJson = TestHelper.readClassPathResource("common/invalid_request_response.json");

        mockMvc.perform(get("/get/{location_id}/{date}", 1, "2020010")
                .header("Auth-Token", "auth-token"))
                .andExpect(status().isBadRequest())
                .andExpect(header().exists("X-Request-ID"))
                .andExpect(content().json(resJson));
    }

    @Test
    void getWeatherInternalServerErrorTest() throws Exception {
        String resJson = TestHelper.readClassPathResource("common/server_error_response.json");

        Mockito.when(weatherService.getWeather(anyString(), anyInt()))
            .thenThrow(new APIException(Errors.INTERNAL_SERVER_ERROR));

        mockMvc.perform(get("/get/1/20200101")
                .header("Auth-Token", "auth-token"))
                .andExpect(status().isInternalServerError())
                .andExpect(header().exists("X-Request-ID"))
                .andExpect(content().json(resJson));
    }

    @Test
    void getWeatherNotFoundErrorTest() throws Exception {
        String resJson = TestHelper.readClassPathResource("weather/get_weather_not_found_response.json");

        Mockito.when(weatherService.getWeather(anyString(), anyInt()))
            .thenThrow(new APIException(Errors.WEATHER_NOT_FOUND));

        mockMvc.perform(get("/get/1/20200101")
                .header("Auth-Token", "auth-token"))
                .andExpect(status().isBadRequest())
                .andExpect(header().exists("X-Request-ID"))
                .andExpect(content().json(resJson));
    }

    @Test
    void getWeatherFilterTest() throws Exception {
        String resJson = TestHelper.readClassPathResource("common/unauthorized_error_response.json");
        mockMvc.perform(get("/get/1/20200101")
                .header("X-Request-ID", "AAA"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(resJson))
                .andExpect(header().string("X-Request-ID", "AAA"));
    }
}
