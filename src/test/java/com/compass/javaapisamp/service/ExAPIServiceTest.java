package com.compass.javaapisamp.service;

import com.compass.javaapisamp.infrastructure.ExAPI;
import com.compass.javaapisamp.model.dto.ExAPIResponse;
import com.compass.javaapisamp.model.exception.APIException;
import com.compass.javaapisamp.model.exception.Errors;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ExAPIServiceTest {

    @MockBean
    private ExAPI mockExAPI;

    @Autowired
    private ExAPIService exAPIService;

    @Test
    void getExWeather_Success() {
        Mockito.when(mockExAPI.getExWeather()).thenReturn(Mono.just(new ExAPIResponse()));
        assertDoesNotThrow(() -> exAPIService.getExWeather());
    }

    @Test
    void getExWeather_Error() {
        Mockito.when(mockExAPI.getExWeather()).thenThrow(new APIException(Errors.INTERNAL_SERVER_ERROR));
        assertThrows(APIException.class, () -> exAPIService.getExWeather());
    }
}
