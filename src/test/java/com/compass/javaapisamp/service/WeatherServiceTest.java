package com.compass.javaapisamp.service;

import com.compass.javaapisamp.model.dto.RegisterRequest;
import com.compass.javaapisamp.model.entity.Weather;
import com.compass.javaapisamp.model.exception.APIException;
import com.compass.javaapisamp.model.exception.Errors;
import com.compass.javaapisamp.repository.WeatherManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class WeatherServiceTest {

    @MockBean
    private WeatherManager mockWeatherManager;

    @Autowired
    private WeatherService weatherService;

    @Test
    void register_Success() {
        Mockito.when(mockWeatherManager.save(any())).thenReturn(any());
        assertDoesNotThrow(() -> weatherService.register(new RegisterRequest(1,"20200101",1, "")));
    }

    @Test
    void register_Error() {
        Mockito.when(mockWeatherManager.save(any())).thenThrow(RuntimeException.class);
        APIException e = assertThrows(APIException.class, () -> weatherService.register(new RegisterRequest(1,"20200101",1, "")));
        assertEquals(Errors.INTERNAL_SERVER_ERROR, e.getError());
    }

    @Test
    void getWeather_Success() {
        Weather expected = new Weather();
        Mockito.when(mockWeatherManager.findById(any())).thenReturn(Optional.of(expected));
        assertEquals(expected, weatherService.getWeather("20200101",1));
    }

    @Test
    void getWeather_NoSuchElement() {
        Mockito.when(mockWeatherManager.findById(any())).thenReturn(Optional.empty());
        APIException e = assertThrows(APIException.class, () -> weatherService.getWeather("20200101", 1));
        assertEquals(Errors.WEATHER_NOT_FOUND, e.getError());
    }

    @Test
    void getWeather_ServiceError() {
        Mockito.when(mockWeatherManager.findById(any())).thenThrow(RuntimeException.class);
        APIException e = assertThrows(APIException.class, () -> weatherService.getWeather("20200101", 1));
        assertEquals(Errors.INTERNAL_SERVER_ERROR, e.getError());
    }
}
