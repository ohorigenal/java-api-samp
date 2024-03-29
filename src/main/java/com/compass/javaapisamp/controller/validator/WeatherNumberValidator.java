package com.compass.javaapisamp.controller.validator;

import com.compass.javaapisamp.model.WeatherCode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WeatherNumberValidator implements ConstraintValidator<WeatherNumber, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return WeatherCode.isExist(value);
    }
}
