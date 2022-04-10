package com.compass.javaapisamp.controller.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherNumberValidatorTest {

    @ParameterizedTest
    @CsvSource({
        "true, 1",
        "false, 0"
    })
    void validatorTest(boolean expected, int num) {
        WeatherNumberValidator validator = new WeatherNumberValidator();
        assertEquals(expected, validator.isValid(num, null));
    }
}
