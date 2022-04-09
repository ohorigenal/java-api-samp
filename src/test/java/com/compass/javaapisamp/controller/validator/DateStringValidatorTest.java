package com.compass.javaapisamp.controller.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateStringValidatorTest {

    @ParameterizedTest(name = "{0}")
    @CsvSource({
        "正常系, 20200101, true",
        "異常系(文字数7), 2020101, false",
        "異常系(文字数9), 202001011, false",
        "異常系(数値でない), 2020T011, false",
        "異常系(数値でない), 2020T011, false"
    })
    void validatorTest(String name, String value, boolean expected) {
        DateStringValidator validator = new DateStringValidator();
        assertEquals(expected, validator.isValid(value, null));
    }
}
