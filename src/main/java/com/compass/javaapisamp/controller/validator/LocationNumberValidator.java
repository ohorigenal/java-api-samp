package com.compass.javaapisamp.controller.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocationNumberValidator implements ConstraintValidator<LocationNumber, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // TODO
        return true;
    }
}
