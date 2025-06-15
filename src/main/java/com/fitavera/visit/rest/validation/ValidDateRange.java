package com.fitavera.visit.rest.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "Invalid date range: dateFrom must be before or equal to dateTo and period must not exceed {maxDays} days";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int maxDays() default 7;
} 