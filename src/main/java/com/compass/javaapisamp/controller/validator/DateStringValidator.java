package com.compass.javaapisamp.controller.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateStringValidator implements ConstraintValidator<DateString, String> {

    @Override
    public void initialize(DateString constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyyMMdd"));
        }catch (Exception e) {
            return false;
        }
        return true;
    }
}
