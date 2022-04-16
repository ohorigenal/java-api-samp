package com.compass.javaapisamp.repository;

import com.compass.javaapisamp.model.entity.Weather;
import com.compass.javaapisamp.model.entity.WeatherID;
import com.compass.javaapisamp.model.exception.APIException;
import com.compass.javaapisamp.model.exception.Errors;
import com.compass.javaapisamp.repository.manager.WeatherManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.QueryTimeoutException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class WeatherRepositoryTest {

    @MockBean
    private WeatherManager weatherManager;

    @Autowired
    private WeatherRepository weatherRepository;

    /*
     * findById
     */
    @Test
    void findWeatherNormalTest() {
        Weather expected = new Weather();
        Mockito.when(weatherManager.findById(any())).thenReturn(Optional.of(expected));
        Weather actual = weatherRepository.findById(new WeatherID());
        assertEquals(expected, actual);
    }

    @Test
    void findWeatherDataAccessErrorTest() {
        Mockito.when(weatherManager.findById(any())).thenThrow(DataRetrievalFailureException.class);
        APIException e = assertThrows(APIException.class, () -> weatherRepository.findById(new WeatherID()));
        assertEquals(Errors.INTERNAL_SERVER_ERROR, e.getError());
    }

    @Test
    void findWeatherNoSuchElementErrorTest() {
        Mockito.when(weatherManager.findById(any())).thenReturn(Optional.empty());
        APIException e = assertThrows(APIException.class, () -> weatherRepository.findById(new WeatherID()));
        assertEquals(Errors.WEATHER_NOT_FOUND, e.getError());
    }

    @Test
    void findWeatherTimeoutRetryErrorTest() {
        Mockito.when(weatherManager.findById(any())).thenThrow(QueryTimeoutException.class);
        APIException e = assertThrows(APIException.class, () -> weatherRepository.findById(new WeatherID()));
        assertEquals(Errors.INTERNAL_SERVER_ERROR, e.getError());

        Mockito.verify(weatherManager, Mockito.times(2)).findById(any());
    }

    /*
     * saveWeather
     */
    @Test
    void saveWeatherNormalTest() {
        Mockito.when(weatherManager.save(any())).thenReturn(new Weather());
        assertDoesNotThrow(() -> weatherRepository.saveWeather(new Weather()));
    }

    @Test
    void saveWeatherDataAccessErrorTest() {
        Mockito.when(weatherManager.save(any())).thenThrow(DataRetrievalFailureException.class);
        APIException e = assertThrows(APIException.class, () -> weatherRepository.saveWeather(new Weather()));
        assertEquals(Errors.INTERNAL_SERVER_ERROR, e.getError());
    }

    @Test
    void saveWeatherTimeoutRetryErrorTest() {
        Mockito.when(weatherManager.save(any())).thenThrow(QueryTimeoutException.class);
        APIException e = assertThrows(APIException.class, () -> weatherRepository.saveWeather(new Weather()));
        assertEquals(Errors.INTERNAL_SERVER_ERROR, e.getError());

        Mockito.verify(weatherManager, Mockito.times(2)).save(any());
    }
}
