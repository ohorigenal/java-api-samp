package com.compass.javaapisamp.model.dto;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

// 個々の詳しいバリデーションチェックはそれぞれのバリデーションクラスで実施
// 異常系はパラメータあたり一つだけテストを作成し、主にメッセージを確認している
public class RegisterRequestTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void normalTest() {
        RegisterRequest request = new RegisterRequest(
            1,
            "20200101",
            1,
            "test comment"
        );
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertEquals(0, violations.size());
    }

    // TODO
    @Test
    void locationErrorTest() {}

    @Test
    void dateErrorTest() {
        RegisterRequest request = new RegisterRequest(
            1,
            "2020010A",
            1,
            "test comment"
        );
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("日付フォーマットが間違っています(例:20220101)", violations.stream().findFirst().get().getMessage());
    }

    @Test
    void weatherErrorTest() {
        RegisterRequest request = new RegisterRequest(
            1,
            "20200101",
            0,
            "test comment"
        );
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("ウェザーコードが存在しません", violations.stream().findFirst().get().getMessage());
    }
}
