package com.fitavera.visit.rest.validation;

import com.fitavera.visit.rest.model.ScheduleViewRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.temporal.ChronoUnit;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, ScheduleViewRequest> {

    private int maxDays;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.maxDays = constraintAnnotation.maxDays();
    }
      
    @Override
    public boolean isValid(ScheduleViewRequest request, ConstraintValidatorContext context) {
        if (request == null || request.dateFrom() == null || request.dateTo() == null) {
            return true;
        }

        if (request.dateFrom().isAfter(request.dateTo())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("dateFrom must be before or equal to dateTo")
                    .addConstraintViolation();
            return false;
        }

        long daysBetween = ChronoUnit.DAYS.between(request.dateFrom(), request.dateTo());
        if (daysBetween > maxDays) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Period must not exceed " + maxDays + " days")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
} 