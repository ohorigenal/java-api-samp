package com.compass.javaapisamp.controller.validator;

import com.compass.javaapisamp.model.WeatherEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WeatherNumberValidator implements ConstraintValidator<WeatherNumber, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return WeatherEnum.isExist(value);
    }
}
