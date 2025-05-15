package com.fitnycrm.common.validation.locale;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SupportedLocaleValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportedLocale {

    String message() default "Unsupported locale";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
