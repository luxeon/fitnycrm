package com.fitavera.common.validation.enumeration;

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

        boolean isValid = acceptedValues.containsAll(values);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Invalid values provided. Allowed values are: " + String.join(", ", acceptedValues))
                    .addConstraintViolation();
        }
        return isValid;
    }
} 