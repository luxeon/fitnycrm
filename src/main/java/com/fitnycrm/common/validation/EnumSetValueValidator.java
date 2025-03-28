package com.fitnycrm.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumSetValueValidator implements ConstraintValidator<EnumSetValue, Set<String>> {
    private Set<String> acceptedValues;

    @Override
    public void initialize(EnumSetValue annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Set<String> values, ConstraintValidatorContext context) {
        if (values == null) {
            return true;
        }
        return acceptedValues.containsAll(values);
    }
} 