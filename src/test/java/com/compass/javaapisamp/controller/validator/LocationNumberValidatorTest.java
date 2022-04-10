package com.compass.javaapisamp.controller.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationNumberValidatorTest {

    //TODO
    @Test
    void validationTest() {
        LocationNumberValidator validator = new LocationNumberValidator();
        assertEquals(true, validator.isValid(1, null));
    }

}
