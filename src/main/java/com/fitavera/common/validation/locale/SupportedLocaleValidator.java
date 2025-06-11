package com.fitavera.common.validation.locale;

import com.fitavera.common.localization.LocalizationProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.lang.String.*;

@Component
@RequiredArgsConstructor
public class SupportedLocaleValidator implements ConstraintValidator<SupportedLocale, String> {

    private final LocalizationProperties localizationProperties;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        boolean isValid = localizationProperties.getSupportedLocales().contains(value);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Allowed locales are: " + join(", ", localizationProperties.getSupportedLocales()))
                    .addConstraintViolation();
        }
        return isValid;
    }
}
