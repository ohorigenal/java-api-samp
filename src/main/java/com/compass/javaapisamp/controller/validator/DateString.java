package com.compass.javaapisamp.controller.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {DateStringValidator.class})
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface DateString {

    String message() default "日付フォーマットが間違っています(例:20220101)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
